package com.ingegneria.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.sp

@Composable
fun Home(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        TopAppBar()
        CharacterStats()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar () {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shop button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Social button"
                        )
                    }
                }
            )
        },
    ) {padding ->
        Column (
            modifier = Modifier.padding(padding)
        ) {}
    }

}

@Composable
fun CharacterStats() {
    val lvlExample = 12
    val currentHpExample = 100
    val currentXpExample = 140
    val maxHp = 300
    val maxXp = 200
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 130.dp, start = 15.dp, end = 15.dp)
    ) {
        Box (
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$lvlExample",
                fontSize = 50.sp
            )
        }
        Column (
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            // TODO: show text over the right corner of the bar
            Text(
                text = "$currentHpExample/$maxHp",
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.End)
            )
            LinearProgressIndicator(
                progress = { (currentHpExample/maxHp.toFloat()) },
                modifier = Modifier.fillMaxWidth().height(15.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background
            )
            Text(
                text = "$currentXpExample/$maxXp",
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.End)
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
            LinearProgressIndicator(
                progress = { (currentXpExample/maxXp.toFloat()) },
                modifier = Modifier.fillMaxWidth().height(15.dp),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.background
            )
            /*
            Box (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(width = 300.dp, height = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue)
            )*/

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome(navController: NavController = rememberNavController()){
    Home(navController)
}