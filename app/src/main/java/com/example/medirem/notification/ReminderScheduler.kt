package com.example.medirem.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.medirem.data.Medicine
import java.util.Calendar

object ReminderScheduler {

    fun scheduleMedicine(context: Context, medicine: Medicine) {
        val timeParts = medicine.time.split(":")
        val dateParts = medicine.date.split("-")
        if (timeParts.size != 2 || dateParts.size != 3) return

        val hour = timeParts[0].toIntOrNull() ?: return
        val minute = timeParts[1].toIntOrNull() ?: return
        val year = dateParts[0].toIntOrNull() ?: return
        val month = dateParts[1].toIntOrNull()?.minus(1) ?: return
        val day = dateParts[2].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(year, month, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            // Already passed
            return
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
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // If we can't schedule exact alarms, fallback to non-exact or just don't schedule
                // Ideally, the app should request permission before this.
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
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