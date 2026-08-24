package com.github.indigogal.practica3

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

fun scheduleNotification(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Configura la hora deseada
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)

        // Si la hora elegida ya pasó hoy, se programa para mañana
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1)
        }
    }

    // Validación de seguridad
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //Consulta directamente al os para ver si la app tiene permisos de agendar alarmas
        if (alarmManager.canScheduleExactAlarms()) {
            //Caso ideal: si tiene permisos se programa la alarma
            alarmManager.setExactAndAllowWhileIdle(
                //Dice que el reloj se basará en la hora real del dispositivo y que despertará  al telefono si esta bloqueado o en reposo
                AlarmManager.RTC_WAKEUP,
                //La hora exacta en milisegundos en la que debe dispararse el evento
                calendar.timeInMillis,
                //La señal guardada que se ejecutará cuando se cumpla la hora (llama a AlarmReceiver)
                pendingIntent
            )
        } else {
            //Caso  no hay permiso de alarma exacta, falla por margen de minutos
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    } else {
        //Caso de versiones antiguas
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}