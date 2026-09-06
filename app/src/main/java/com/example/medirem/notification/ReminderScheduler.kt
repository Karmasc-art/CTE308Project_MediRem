package com.example.medirem.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.medirem.data.Medicine
import java.util.Calendar

object ReminderScheduler {

    fun scheduleMedicine(context: Context, medicine: Medicine) {
        val parts = medicine.time.split(":")
        if (parts.size != 2) return

        val hour = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, MedicineReminderReceiver::class.java).apply {
            putExtra("medicine_id", medicine.id)
            putExtra("medicine_name", medicine.name)
            putExtra("dosage", medicine.dosage)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicine.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelMedicine(context: Context, medicineId: Int) {
        val intent = Intent(context, MedicineReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicineId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}