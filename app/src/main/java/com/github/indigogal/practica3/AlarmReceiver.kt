package com.github.indigogal.practica3

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

//Clase que hereda de la clase Broadcast para poder escuchar eventos o señales en el sistema
class AlarmReceiver : BroadcastReceiver() {
    //metodo que se ejecuta al activarse la alarma
    override fun onReceive(context: Context, intent: Intent) {
        val chanId = "canal_record"
        //Solicita el servicio de gestionar alertas y notificaciones, conviertiendo el servicio génerico a NotificationManager para su uso
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Comprueba que el dispositivo tiene Android 8 o superior para permitir la ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Crea el Objeto NotificationChannel con id, nombre e importancia alta, para mostrar un pop-up y que tenga sonido
            val canal = NotificationChannel(chanId, "Recordatorio", NotificationManager.IMPORTANCE_HIGH)
            //Registra el canal para el sistema
            notificationManager.createNotificationChannel(canal)
        }
        //Usa el Builder para ser compatibles con versiones antiguas
        val notification = NotificationCompat.Builder(context, chanId)
            //Pone el icono de la app en la notificación
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            //Pone titulo
            .setContentTitle("Recordatorio")
            //Contenido de la notificación
            .setContentText("Notificación")
            //Garantiza las funciones visuales y sonoras de la notificación poniendo su prioridad como alta
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //Elminar la notificación despues de clickear en ella
            .setAutoCancel(true)
            //JUnta toda las configuraciones y las convierte en un objeto Notification
            .build()
        //Envía la notificación
        notificationManager.notify(1, notification)
    }
}