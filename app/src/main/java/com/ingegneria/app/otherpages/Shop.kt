package com.ingegneria.app.otherpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AirlineSeatFlat
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(navController: NavController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column (modifier = Modifier.fillMaxWidth()) {
                topTabs()
            } // TODO: center tabs

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
    }
}

@Composable
fun topTabs() {
    var tabs_state by remember { mutableStateOf(0) }
    val tabs = listOf("Shop", "Items")
//    val tabs_selected_icons = listOf(Icons.Filled.ShoppingCart, Icons.Filled.AirlineSeatFlat)
//    val tabs_unselected_icons = listOf(Icons.Outlined.ShoppingCart, Icons.Outlined.AirlineSeatFlat)
    TabRow(selectedTabIndex = tabs_state,
        modifier = Modifier
            .padding(vertical = 15.dp)
            .size(width = 300.dp, 30.dp)
            .padding(1.dp)
            .clip(RoundedCornerShape(50)),
        indicator = { tabPositions: List<TabPosition> ->
            Box(modifier = Modifier.size(width = 100.dp, height = 30.dp)
                ){

            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = tabs_state == index
            Tab(
                modifier = if(selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onBackground)
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.background),
                selected = selected,
                onClick = { tabs_state = index },
                text = { Text(tab) },
            )
        }
    }
}
/*
icon = {
    if(tabs_state == index) {
        Icon(tabs_selected_icons[index], tab)
    } else {
        Icon(tabs_unselected_icons[index], tab)
    }
}
*/

@Preview(showBackground = true)
@Composable
fun ShopPreview(navController: NavController = rememberNavController()) {
    AppTheme { Shop(navController = navController) }
}