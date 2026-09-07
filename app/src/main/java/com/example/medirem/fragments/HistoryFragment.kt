package com.example.medirem.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medirem.R
import com.example.medirem.adapter.MedicineAdapter
import com.example.medirem.data.MediRemDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var selectedDate: String = ""
    private lateinit var tvDateLabel: TextView
    private lateinit var recyclerHistory: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDateLabel = view.findViewById(R.id.tvDateLabel)
        recyclerHistory = view.findViewById(R.id.recyclerHistory)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val calendarCard = view.findViewById<View>(R.id.calendarCard)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        recyclerHistory.layoutManager = LinearLayoutManager(requireContext())

        // Default to today
        val calendar = Calendar.getInstance()
        updateSelectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        calendarCard.setOnClickListener { showDatePicker() }
        tvDateLabel.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            updateSelectedDate(year, month, day)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateSelectedDate(year: Int, month: Int, day: Int) {
        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
        
        val displayFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        tvDateLabel.text = displayFormat.format(cal.time)

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val medicines = db.medicineDao().getMedicinesByDate(selectedDate)
            recyclerHistory.adapter = MedicineAdapter(medicines) {
                // Show details if needed
            }
        }
    }
}