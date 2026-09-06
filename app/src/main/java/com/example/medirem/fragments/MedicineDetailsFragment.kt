package com.example.medirem.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.medirem.R
import com.example.medirem.data.MediRemDatabase
import kotlinx.coroutines.launch

class MedicineDetailsFragment : Fragment(R.layout.fragment_medicine_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicineId = arguments?.getInt("medicineId") ?: return

        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val medicine = db.medicineDao().getMedicine(medicineId) ?: return@launch

            view.findViewById<TextView>(R.id.tvName).text = medicine.name
            view.findViewById<TextView>(R.id.tvDosage).text = medicine.dosage
            view.findViewById<TextView>(R.id.tvTime).text = medicine.time
            view.findViewById<TextView>(R.id.tvNote).text = medicine.note
        }
    }
}