package com.ingegneria.app.otherpages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(navController: NavController, shopVM: ShopViewModel) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column (modifier = Modifier.fillMaxWidth()) {
                topTabs()
            }
            TextButton(
                modifier = Modifier
                    .padding(start = 7.dp, top = 10.dp)
                    .wrapContentSize(),
                onClick = {navController.navigate(Screens.Home.name)}
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                )
            }
        }
    ){ padding ->
        LazyColumn (
            modifier = Modifier.padding(top = 120.dp)
        ){
            items(shopVM.shopItems) { item ->
                Card(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ){
                    Row (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = item,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun topTabs() {
    var tabs_state by remember { mutableStateOf(0) }
    val tabs = listOf("Shop", "Items")

    TabRow(
        selectedTabIndex = tabs_state,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabs_state])
                    .padding(horizontal = 20.dp)
                    .height(2.dp)
                    .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = tabs_state == index
            Tab(
                modifier = Modifier.padding(top = 50.dp),
                selected = selected,
                onClick = { tabs_state = index },
                text = { Text(tab) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopPreview(navController: NavController = rememberNavController()) {
//    AppTheme { Shop(navController = navController) }
}