package com.example.medirem.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medirem.data.MediRemDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicineReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getIntExtra("medicine_id", -1)
        val medicineName = intent.getStringExtra("medicine_name") ?: "Medicine"
        val dosage = intent.getStringExtra("dosage") ?: ""

        NotificationHelper.showMedicineNotification(
            context,
            medicineName,
            dosage,
            medicineId
        )

        // Reschedule the next alarm
        if (medicineId != -1) {
            val pendingResult = goAsync()
            val db = MediRemDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val medicine = db.medicineDao().getMedicine(medicineId)
                    medicine?.let {
                        ReminderScheduler.scheduleMedicine(context, it)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}