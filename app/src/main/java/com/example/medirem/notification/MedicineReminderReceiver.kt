package com.example.medirem.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

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
    }
}