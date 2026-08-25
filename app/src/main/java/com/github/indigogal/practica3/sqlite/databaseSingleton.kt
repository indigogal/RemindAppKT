package com.github.indigogal.practica3.sqlite

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(entities = [Reminder::class], version=1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun reminderDao(): ReminderEntityDao
}

object ReminderDB{
    private var instance: AppDatabase? = null

    fun get(ctx: Context): AppDatabase{
        if (instance == null){
            instance = Room.databaseBuilder(ctx, AppDatabase::class.java,"app_db").build()
        }
        return instance!!
    }
}