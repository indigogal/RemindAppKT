package com.github.indigogal.practica3

import android.app.TimePickerDialog
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun Recordatorios(
    modifier: Modifier = Modifier
) {
    // Obtiene el Context de Android necesario para invocar servicios del sistema y diálogos nativos
    val context = LocalContext.current

    // Estado para actualizar el texto descriptivo de la hora seleccionada en la UI
    var selectedTimeText by remember { mutableStateOf("Pon un recordatorio importante") }

    // Registra el manejador de permisos para solicitar POST_NOTIFICATIONS en tiempo de ejecución
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Callback opcional al aceptar o denegar el permiso */ }
    )

    // Solicita el permiso automáticamente al cargar la pantalla en Android 13+ (API 33)
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Instancia el diálogo nativo de selección de hora del sistema Android (compatible con Oreo y versiones anteriores)
    val timePickerDialog = remember {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // Se ejecuta cuando el usuario presiona "Aceptar" en el reloj nativo
                scheduleNotification(context, hourOfDay, minute)

                // Formatea los minutos a dos dígitos (ejemplo: "05" en lugar de "5")
                val formattedMinute = String.format("%02d", minute)
                selectedTimeText = "Recordatorio guardado para las $hourOfDay:$formattedMinute"
            },
            calendar.get(Calendar.HOUR_OF_DAY), // Hora inicial por defecto
            calendar.get(Calendar.MINUTE),      // Minuto inicial por defecto
            false                               // false = Formato 12 horas (AM/PM), true = 24 horas
        )
    }

    // Tarjeta contenedora de la interfaz
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Programar Recordatorio",
                style = MaterialTheme.typography.titleMedium
            )

            // Muestra la hora programada actual o el estado de la prueba
            Text(
                text = selectedTimeText,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón principal que abre el TimePickerDialog nativo del sistema
                Button(onClick = { timePickerDialog.show() }) {
                    Text("Seleccionar Hora")
                }

                // Botón secundario para lanzar una alarma de prueba rápida en 5 segundos
                OutlinedButton(
                    onClick = {
                        scheduleTestNotification(context, delayInSeconds = 5)
                        selectedTimeText = "Prueba lanzada (sonará en 5 seg)"
                    }
                ) {
                    Text("Probar (5s)")
                }
            }
        }
    }
}

// Función auxiliar privada para calcular y programar una notificación en N segundos
private fun scheduleTestNotification(context: android.content.Context, delayInSeconds: Int) {
    // Suma los segundos indicados a la hora actual del reloj
    val calendar = Calendar.getInstance().apply {
        add(Calendar.SECOND, delayInSeconds)
    }

    // Reutiliza la función existente pasando la hora y minutos calculados
    scheduleNotification(
        context = context,
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE)
    )
}