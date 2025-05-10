package com.example.basketball.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.basketball.R
import com.example.basketball.data.local.model.ScheduleResponse
import com.example.basketball.data.local.model.Team
import com.example.basketball.domain.utils.filterCategories
import com.example.basketball.presentation.FilterBottomSheet
import com.example.basketball.data.local.model.TeamInfo
import com.example.basketball.domain.utils.ApiState
import com.example.basketball.domain.utils.Utility
import com.example.basketball.presentation.ShowLoading
import com.example.basketball.presentation.screens.cutomwidgets.GameRowItem
import com.example.basketball.presentation.screens.cutomwidgets.MonthYearDropdown
import com.example.basketball.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var functionCalled by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedCategories = remember { mutableStateListOf<String>() }
   // val filters = remember { mutableStateListOf<FilterType>() }



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBottomSheet = true
                selectedCategories.clear()

            }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter")
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
             TeamHeaderWithTabs(0, mainViewModel) {
             }
            LaunchedEffect(Unit) {
                mainViewModel.getScheduleData()
            }


            if (showBottomSheet) {
                FilterBottomSheet(
                    filterCategories = filterCategories,
                    onCategorySelected = { category ->
                        if (category in selectedCategories) {
                            selectedCategories.remove(category)
                        } else {
                            selectedCategories.add(category)
                        }
                        functionCalled = true
                    },
                    onDismissRequest = { showBottomSheet = false }
                )
            }

            LaunchedEffect(functionCalled) {
                if (functionCalled) {

                    functionCalled = false // Reset the flag
                }
            }
        }
    }
}

@Composable
fun TeamHeaderWithTabs(
    selectedTabIndex: Int,
    mainViewModel: MainViewModel = hiltViewModel(),
    onTabSelected: (Int) -> Unit
) {
    val scheduleState by mainViewModel.scheduleState.collectAsState()
    val teamsState by mainViewModel.teamsState.collectAsState()

    var selectedMonth by remember { mutableStateOf("JULY 2023") }
    val allMonths = listOf("JUNE 2023", "JULY 2023", "AUGUST 2023")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        // Header Title
        Text(
            text = stringResource(id = R.string.header_team),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        )

        // Tabs
        val tabs = listOf(stringResource(id = R.string.tab_01), stringResource(id = R.string.tab_02))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Black,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp)
                        .background(Color.White),
                    color = Color.Yellow
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.White else Color.Gray
                        )
                    }
                )
            }
        }
        MonthYearDropdown(
            selectedMonthYear = selectedMonth,
            monthYearOptions = allMonths,
            onMonthYearSelected = { selectedMonth = it }
        )

        when (scheduleState) {
            is ApiState.Loading -> ShowLoading()
            is ApiState.Success -> {
                val scheduleData = (scheduleState as ApiState.Success).data
                LaunchedEffect(scheduleState) {
                    mainViewModel.getTeams()
                }
                when(teamsState){
                    is ApiState.Loading -> ShowLoading()
                    is ApiState.Success -> {
                        val teamsData = (teamsState as ApiState.Success).data
                          showScheduleList(scheduleData, teamsData)
                    }
                    is ApiState.Failure -> Text("Error: ${(teamsState as ApiState.Failure).msg.message}")
                    ApiState.Empty -> Text("No data")
                }
            }
            is ApiState.Failure -> Text("Error: ${(scheduleState as ApiState.Failure).msg.message}")
            ApiState.Empty -> Text("No data")
        }



    }
}
@Composable
fun showScheduleList(data: ScheduleResponse, teamsList: List<Team>, mainViewModel: MainViewModel = hiltViewModel()){

    val searchItem = remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        item {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = Color.White),
            value = searchItem.value,
            onValueChange = { searchText ->
                searchItem.value = searchText
                mainViewModel.getFilterListByAutherTeamPlace(data.data.schedules, searchText)

            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            })
    }
        items(data.data.schedules) { item ->
            //Text("Arena: ${item.arena_name}, Game Time: ${item.gametime}")
            GameRowItem(
                autherName = item.arena_name,
                city = item.arena_city,
                teamName = Utility.getTeam(item.h.tid, teamsList),
                gameStatus = item.st,
                stt = item.stt,
                date = item.gametime,
                statusList = item.bd?.b,
                homeTeam = TeamInfo(tid = item.h.tid, tn = item.h.tn, ta = item.h.ta, tc = item.h.tc, logo = Utility.getTeamLogo(item.h.tid, teamsList), s = item.h.s),
                awayTeam = TeamInfo(tid = item.v.tid, tn = item.v.tn, ta = item.v.ta, tc = item.v.tc, logo = Utility.getTeamLogo(item.v.tid, teamsList),s = item.v.s),
            )
        }
    }
}


