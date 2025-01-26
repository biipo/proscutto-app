package com.ingegneria.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingegneria.app.models.Pet

@Composable
fun HomeStats(pet: Pet?) {
    if (pet != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 90.dp, start = 15.dp, end = 15.dp)
            ) {
                Text(
                    text = pet.name,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            PetStats(pet)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
            ) {
                MascotImageBig(pet.hat!!)
            }
        }
    }
}

@Composable
fun PetStats(pet: Pet?) {
    if (pet != null) {
        Row(
            modifier = Modifier
                .padding(top = 30.dp, start = 15.dp, end = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${pet.level}",
                    fontSize = 50.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "${pet.hp}/${pet.maxHp()}",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.End)
                )
                LinearProgressIndicator(
                    progress = { (pet.hp / pet.maxHp().toFloat()) },
                    modifier = Modifier.fillMaxWidth().height(15.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "${pet.xp}/${pet.maxXp()}",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.End)
                        .padding(0.dp, 15.dp, 0.dp, 0.dp)
                )
                LinearProgressIndicator(
                    progress = { (pet.xp / pet.maxXp().toFloat()) },
                    modifier = Modifier.fillMaxWidth().height(15.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}