package com.example.basketball.presentation.screens

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.basketball.R
import com.example.basketball.data.local.model.ScheduleItem
import com.example.basketball.data.local.model.Team
import com.example.basketball.domain.utils.filterCategories
import com.example.basketball.presentation.FilterBottomSheet
import com.example.basketball.data.local.model.TeamInfo
import com.example.basketball.domain.utils.ApiState
import com.example.basketball.domain.utils.Converter
import com.example.basketball.domain.utils.Converter.extractUniqueMonthYearList
import com.example.basketball.domain.utils.Utility
import com.example.basketball.domain.utils.Utility.filterUsersByGameType
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
    var selectedTabIndex by remember { mutableStateOf(0) }

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
            TeamHeaderWithTabs(selectedTabIndex =  selectedTabIndex, onTabSelected = {
                selectedTabIndex = it
            })
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
                    mainViewModel.getFilterListByOption(
                        filterUsersByGameType(selectedCategories.toList())
                    )
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

    var selectedMonth by remember { mutableStateOf("MAY 2025") }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )

        // Tabs
        val tabs =
            listOf(stringResource(id = R.string.tab_01), stringResource(id = R.string.tab_02))

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

        when (val result = mainViewModel.response.value) {
            is ApiState.Loading -> ShowLoading()
            is ApiState.Success -> {
                val scheduleList = result.data
                val monthYearList = extractUniqueMonthYearList(scheduleList)

                MonthYearDropdown(
                    selectedMonthYear = selectedMonth,
                    monthYearOptions = monthYearList,
                    onMonthYearSelected = {
                        selectedMonth = it
                    }
                )

                LaunchedEffect(Unit) {
                    mainViewModel.getTeams()
                }
                when (teamsState) {
                    is ApiState.Loading -> ShowLoading()
                    is ApiState.Success -> {
                        val teamsData = (teamsState as ApiState.Success).data
                        showScheduleList(result.data, teamsData)
                    }

                    is ApiState.Failure -> Text("Error: ${(teamsState as ApiState.Failure).msg.message}")
                    ApiState.Empty -> Text("No data 1")
                }
            }

            is ApiState.Failure -> Text("Error: ${(scheduleState as ApiState.Failure).msg.message}")
            ApiState.Empty -> Text("No data 2")
        }


    }
}

@Composable
fun showScheduleList(
    data: List<ScheduleItem>,
    teamsList: List<Team>,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val viewState by mainViewModel.scheduleState.collectAsState()
    val searchItem = remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ,
        value = searchItem.value,
        onValueChange = { searchText ->
            searchItem.value = searchText
            mainViewModel.getFilterListByAutherTeamPlace(data, searchText)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White)
        },
        textStyle = TextStyle(color = Color.White),
        placeholder = {
            Text(
                "Search", color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    )

    if (viewState.filterList.isEmpty()) {
        Text(
            text = stringResource(id = R.string.data_not_found),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold

        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (viewState.filterList.isNotEmpty()) items(items = viewState.filterList) { item ->
                GameRowItem(
                    autherName = item.arena_name,
                    city = item.arena_city,
                    teamName = Utility.getTeam(item.h.tid, teamsList),
                    gameStatus = item.st,
                    stt = item.stt,
                    date = item.gametime,
                    statusList = item.bd?.b,
                    homeTeam = TeamInfo(
                        tid = item.h.tid,
                        tn = item.h.tn,
                        ta = item.h.ta,
                        tc = item.h.tc,
                        logo = Utility.getTeamLogo(item.h.tid, teamsList),
                        s = item.h.s
                    ),
                    awayTeam = TeamInfo(
                        tid = item.v.tid,
                        tn = item.v.tn,
                        ta = item.v.ta,
                        tc = item.v.tc,
                        logo = Utility.getTeamLogo(item.v.tid, teamsList),
                        s = item.v.s
                    ),
                )
            }
        }
    }
}


