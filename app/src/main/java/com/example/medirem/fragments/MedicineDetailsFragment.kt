package com.example.medirem.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.MediRemDatabase
import com.example.medirem.notification.ReminderScheduler
import com.example.medirem.utils.TimeUtils
import kotlinx.coroutines.launch

class MedicineDetailsFragment : Fragment(R.layout.fragment_medicine_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicineId = arguments?.getInt("medicineId") ?: return
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Medicine")
                .setMessage("Are you sure you want to delete this medicine schedule?")
                .setPositiveButton("Delete") { _, _ ->
                    lifecycleScope.launch {
                        val db = MediRemDatabase.getDatabase(requireContext())
                        val medicine = db.medicineDao().getMedicine(medicineId)
                        medicine?.let {
                            db.medicineDao().deleteMedicine(it)
                            ReminderScheduler.cancelMedicine(requireContext(), it.id)
                            Toast.makeText(requireContext(), "Medicine Deleted", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvDosage = view.findViewById<TextView>(R.id.tvDosage)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        val tvStartDate = view.findViewById<TextView>(R.id.tvStartDate)
        val tvNote = view.findViewById<TextView>(R.id.tvNote)
        val ivMedicineImage = view.findViewById<ImageView>(R.id.ivMedicineImage)
        val btnMarkTaken = view.findViewById<android.widget.Button>(R.id.btnMarkTaken)

        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val medicine = db.medicineDao().getMedicine(medicineId) ?: return@launch

            tvName.text = medicine.name
            tvDosage.text = medicine.dosage
            tvTime.text = TimeUtils.formatToAmPm(medicine.time)
            tvStartDate.text = medicine.date
            tvNote.text = medicine.note
            
            if (medicine.taken) {
                btnMarkTaken.text = "Mark as Not Taken"
                btnMarkTaken.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.missed_text))
            } else {
                btnMarkTaken.text = "Mark as Taken"
                btnMarkTaken.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_green))
            }

            btnMarkTaken.setOnClickListener {
                lifecycleScope.launch {
                    db.medicineDao().updateTakenStatus(medicine.id, !medicine.taken)
                    findNavController().popBackStack() // Or just refresh
                }
            }

            medicine.photoUri?.let { uriString ->
                try {
                    ivMedicineImage.setImageURI(android.net.Uri.parse(uriString))
                } catch (e: Exception) {
                    ivMedicineImage.setImageResource(R.drawable.ic_medicine)
                }
            }
        }
    }
}