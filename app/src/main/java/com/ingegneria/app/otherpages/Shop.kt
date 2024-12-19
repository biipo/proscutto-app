package com.ingegneria.app.otherpages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            topTabs() // TODO: floating tabs
            Button(
                modifier = Modifier
                    .padding(start = 7.dp, top = 10.dp)
                    .size(50.dp, 50.dp),
                onClick = {navController.navigate(Screens.Home.name)}
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(40.dp)
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
    val tabs_selected_icons = listOf(Icons.Filled.ShoppingCart, Icons.Filled.AirlineSeatFlat)
    val tabs_unselected_icons = listOf(Icons.Outlined.ShoppingCart, Icons.Outlined.AirlineSeatFlat)
    TabRow(selectedTabIndex = tabs_state) {
        tabs.forEachIndexed { index, tab ->
            val selected = tabs_state == index
            Tab(
                selected = tabs_state == index,
                onClick = { tabs_state = index },
                text = { Text(tab) },
                icon = {
                    if(tabs_state == index) {
                        Icon(tabs_selected_icons[index], tab)
                    } else {
                        Icon(tabs_unselected_icons[index], tab)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopPreview(navController: NavController = rememberNavController()) {
    AppTheme { Shop(navController = navController) }
}