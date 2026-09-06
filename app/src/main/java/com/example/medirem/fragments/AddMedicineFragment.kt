package com.example.medirem.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.Medicine
import com.example.medirem.data.MediRemDatabase
import com.example.medirem.notification.ReminderScheduler
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.*

class AddMedicineFragment : Fragment(R.layout.fragment_add_medicine) {

    private var selectedTime: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etDosage = view.findViewById<TextInputEditText>(R.id.etDosage)
        val etNote = view.findViewById<TextInputEditText>(R.id.etNote)
        val btnTime = view.findViewById<MaterialButton>(R.id.btnTime)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        btnTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                btnTime.text = selectedTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val dosage = etDosage.text.toString()
            val note = etNote.text.toString()

            if (name.isEmpty() || dosage.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicine = Medicine(
                name = name,
                dosage = dosage,
                note = note,
                time = selectedTime
            )

            lifecycleScope.launch {
                val db = MediRemDatabase.getDatabase(requireContext())
                db.medicineDao().insertMedicine(medicine)
                
                // Fetch all to find the last inserted (or use a better way in DAO)
                val medicines = db.medicineDao().getAllMedicines()
                val savedMedicine = medicines.lastOrNull()
                
                if (savedMedicine != null) {
                    ReminderScheduler.scheduleMedicine(requireContext(), savedMedicine)
                }

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Medicine Saved", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }
}