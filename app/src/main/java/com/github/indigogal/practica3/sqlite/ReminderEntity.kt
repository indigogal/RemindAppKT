package com.github.indigogal.practica3.sqlite

import androidx.room3.ColumnInfo
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="content") val content: String,
    @ColumnInfo(name="expiresAt") val expiresAt: Int
)

@Dao
interface ReminderEntityDao{
    @Query("SELECT * FROM reminders")
    suspend fun getAll(): List<Reminder>

    @Query("DELETE FROM reminders")
    suspend fun wipeReminders()

    @Delete
    suspend fun removeReminder(reminder: Reminder)

    @Insert
    suspend fun createReminder(vararg reminder: Reminder)

}