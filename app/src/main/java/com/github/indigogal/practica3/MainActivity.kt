package com.github.indigogal.practica3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.indigogal.practica3.sqlite.Reminder
import com.github.indigogal.practica3.sqlite.ReminderDB
import com.github.indigogal.practica3.ui2.theme.AppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Recordatorios()
                            ReminderList()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Carga los recordatorios guardados en la base de datos (a través del singleton
 * [ReminderDB] y su DAO) y los muestra en una lista desplazable.
 */
@Composable
fun ReminderList(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var reminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }

    // Carga inicial de los recordatorios al componer la pantalla
    LaunchedEffect(Unit) {
        seedTestRemindersIfEmpty(context)
        reminders = ReminderDB.get(context).reminderDao().getAll()
    }

    if (reminders.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay recordatorios guardados",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(reminders, key = { it.id }) { reminder ->
                ReminderCard(
                    reminder = reminder,
                ) {
                    scope.launch {
                        ReminderDB.get(context).reminderDao().removeReminder(reminder)
                        reminders = ReminderDB.get(context).reminderDao().getAll()
                    }
                }
            }
        }
    }
}

/**
 * Si la base de datos no contiene recordatorios, inserta tres de prueba
 * (en español) para que la lista tenga contenido visible al abrir la app.
 */
private suspend fun seedTestRemindersIfEmpty(context: android.content.Context) {
    val dao = ReminderDB.get(context).reminderDao()
    if (dao.getAll().isEmpty()) {
        dao.createReminder(
            Reminder(
                id = 0,
                title = "Comprar leche",
                content = "Pasar por el supermercado antes de las 8 pm",
                expiresAt = 123456
            ),
            Reminder(
                id = 0,
                title = "Estudiar Kotlin",
                content = "Repasar corrutinas y Jetpack Compose",
                expiresAt = 234567
            ),
            Reminder(
                id = 0,
                title = "Llamar al médico",
                content = "Agendar cita de control anual",
                expiresAt = 345678
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordPreview(){
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Recordatorios()
                ReminderList()
            }
        }

    }
}
