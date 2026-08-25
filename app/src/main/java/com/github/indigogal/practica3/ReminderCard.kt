package com.github.indigogal.practica3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.github.indigogal.practica3.sqlite.Reminder
import com.github.indigogal.practica3.ui.theme.Practica3Theme

@Composable
fun ReminderCard(
    reminder: Reminder,
    onComplete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Título
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.titleLarge
            )

            // Descripción
            Text(
                text = reminder.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Fecha de expiración
            Text(
                text = "Expira: ${reminder.expiresAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )

            // Botón de completar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onComplete
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Completar recordatorio"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderCardPreview() {
    Practica3Theme {
        ReminderCard(
            reminder = Reminder(
                title = "Estudiar Kotlin",
                content =  "Repasar Jetpack Compose",
                expiresAt = 321424,
                id = 1
            ),
            onComplete = {}
        )
    }
}