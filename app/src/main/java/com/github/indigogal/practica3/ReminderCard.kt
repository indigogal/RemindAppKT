package com.github.indigogal.practica3

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun ReminderCard(){
    var qoutes = arrayOf("1","2","3")
    var b by remember { mutableStateOf(qoutes[0]) }
    var i by remember { mutableStateOf(0) }
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.size(width = 240.dp, height = 100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        if (!b.isEmpty()){
            Text(
                text = b,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        Button(onClick = {
            if (i <2) i++ else i= 0
            b= qoutes[i]
        }) {
            Text("Next quote")
        }
    }
}