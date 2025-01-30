package com.ingegneria.app.ui.otherpages

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.screens.PetViewModel

@Composable
fun Shop(navController: NavController, shopVM: ShopViewModel, petVM: PetViewModel) {

    var currTabState by remember { mutableIntStateOf(0) }
    var openBuyDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    val tabs = listOf("Shop", "Items")

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column (modifier = Modifier.fillMaxWidth()) {
                TopTabs(
                    currTabState = currTabState,
                    tabs = tabs,
                    changeTab = {index: Int ->
                    currTabState = index
                    })
            }
            TextButton(
                modifier = Modifier
                    .padding(start = 7.dp, top = 10.dp)
                    .wrapContentSize(),
                onClick = {navController.popBackStack()}
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                )
            }
        }
    ){ padding ->
        if(currTabState == 0) {
            ShowList(
                itemsList = shopVM.shopItems,
                openBuyDialogAction = { item ->
                    selectedItem = item
                    openBuyDialog = !openBuyDialog
                }
            )
        } else {
            ShowList(
                itemsList = shopVM.userItems,
                openBuyDialogAction = { item ->
                    petVM.petFb?.setHat(item)
                }
            )
        }
    }
    when {
        openBuyDialog -> {
            BuyItemDialog(
                onDismissRequest = { openBuyDialog = false},
                shopVM = shopVM,
                item = selectedItem
            )
        }
    }
}

@Composable
fun ShowList(itemsList: List<String>, openBuyDialogAction: (String) -> Unit) {
    if (itemsList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(top = 120.dp)
        ) {
            items(itemsList) { item ->
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
                    ),
                    onClick = { openBuyDialogAction(item) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Pets,
                            contentDescription = item
                        )
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = item,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 120.dp),
                text = "There are no items :("
            )
        }
    }
}

@Composable
fun TopTabs(currTabState: Int, tabs: List<String>, changeTab: (Int) -> Unit) {

    TabRow(
        selectedTabIndex = currTabState,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[currTabState])
                    .padding(horizontal = 20.dp)
                    .height(2.dp)
                    .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = currTabState == index
            Tab(
                modifier = Modifier.padding(top = 50.dp),
                selected = selected,
                onClick = { changeTab(index) },
                text = { Text(tab) },
            )
        }
    }
}

@Composable
fun BuyItemDialog(onDismissRequest: () -> Unit, item: String, shopVM: ShopViewModel) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Do you want to buy $item?",
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row (modifier = Modifier.padding(vertical = 20.dp)){
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            if (!shopVM.buyItem(item)) {
                                Toast.makeText(context, "You already own that item", Toast.LENGTH_LONG).show()
                            }
                            onDismissRequest.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Buy")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopPreview(navController: NavController = rememberNavController()) {
//    AppTheme { Shop(navController = navController) }
}