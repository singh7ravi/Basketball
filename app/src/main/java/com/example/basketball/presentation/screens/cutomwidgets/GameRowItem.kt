package com.example.basketball.presentation.screens.cutomwidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.basketball.data.local.model.Broadcaster
import com.example.basketball.data.local.model.TeamInfo
import com.example.basketball.domain.utils.Converter.convertEtTimeToDisplay
import com.example.basketball.domain.utils.Converter.convertUtcToLocalDayLabel
import com.example.basketball.presentation.SpacerHeight

@Composable
fun GameRowItem(
    autherName: String,
    city: String,
    teamName : String,
    gameStatus: Int,
    stt: String,
    date: String,
    statusList: List<Broadcaster>?, // e.g., "FINAL"
    homeTeam: TeamInfo,
    awayTeam: TeamInfo,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = statusList?.get(0)?.scope?.toUpperCase() ?: "",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(1.dp)
                        .background(Color.Gray)
                )
                Text(
                    text = convertUtcToLocalDayLabel(date),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
                if(gameStatus != 2){ // Live For two items in row
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .width(1.dp)
                            .background(Color.Gray)
                    )
                    var status02 = ""
                    if(gameStatus != 1 && statusList?.size == 2){
                        status02 = statusList.get(2).scope?.toUpperCase() ?: ""
                    }else{
                        status02 = convertEtTimeToDisplay(stt)
                    }
                    Text(
                        text = status02,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Auther Details
            ArenaTeamRow(autherName, city, teamName)
            Spacer(modifier = Modifier.height(8.dp))

            // Game Row with Logos and Scores
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Away Team
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if(awayTeam.logo.trim().isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(awayTeam.logo.trim())
                                .crossfade(true)
                                .build(),
                            contentDescription = "Team Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Text(
                        text = awayTeam.ta,
                        color = Color.White,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

               Column(horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center) {
                   if(gameStatus == 2)
                       LiveBadge()
                   SpacerHeight(10.dp)
                   // Scores
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center,
                   ) {
                       Text(
                           text = awayTeam.s,
                           color = Color.White,
                           fontWeight = FontWeight.Bold,
                           fontSize = 20.sp
                       )
                       Text(
                           text = if(gameStatus == 3) "@" else "VS",
                           color = Color.White,
                           modifier = Modifier.padding(horizontal = 4.dp)
                       )
                       Text(
                           text = homeTeam.s,
                           color = Color.White,
                           fontWeight = FontWeight.Bold,
                           fontSize = 20.sp
                       )
                   }
               }

                // Home Team
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if(homeTeam.logo.trim().isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(homeTeam.logo.trim())
                                .crossfade(true)
                                .build(),
                            contentDescription = "Team Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Text(
                        text = homeTeam.ta,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp,
                    )
                }
            }
            SpacerHeight(10.dp)
            if(gameStatus == 1) {
                BuyTicketButton()
            }
        }
    }
}

@Composable
fun LiveBadge() {
    Box(
        modifier = Modifier
            .background(color = Color(0xFF171515), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = "LIVE",
            color = Color.LightGray,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        )
    }
}

@Composable
fun BuyTicketButton() {
    Box(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .fillMaxWidth()
    ) {
       Row(horizontalArrangement = Arrangement.Center,
           modifier = Modifier.fillMaxWidth()
           ) {
           Text(
               text = "BUY TICKET ON ",
               color = Color.Black,
               fontWeight = FontWeight.Bold,
               fontSize = 14.sp,
               modifier = Modifier.align(Alignment.CenterVertically)
           )
           Text(
               text = "ticketmaster",
               color = Color.Black,
               fontWeight = FontWeight.Bold,
               fontStyle = FontStyle.Italic,
               fontSize = 12.sp
           )
       }
    }
}

@Composable
fun ArenaTeamRow(
    arenaName: String,
    city: String,
    teamName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Text(
            text = arenaName,
            style = MaterialTheme.typography.labelMedium,
            color = Color.LightGray
        )
        Text(
            text = "$teamName • $city",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
