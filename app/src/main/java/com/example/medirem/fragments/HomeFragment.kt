package com.example.medirem.fragments

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var tvTakenCount: TextView
    private lateinit var tvUpcomingCount: TextView
    private lateinit var tvMissedCount: TextView
    private lateinit var recyclerToday: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTakenCount = view.findViewById(R.id.tvTakenCount)
        tvUpcomingCount = view.findViewById(R.id.tvUpcomingCount)
        tvMissedCount = view.findViewById(R.id.tvMissedCount)
        recyclerToday = view.findViewById(R.id.recyclerToday)

        recyclerToday.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<FloatingActionButton>(R.id.fabAddMedicine).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_add)
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        view.findViewById<View>(R.id.ivSearch).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_list)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodayData()
    }

    private fun loadTodayData() {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val medicines = db.medicineDao().getMedicinesByDate(todayDate)

            var taken = 0
            var upcoming = 0
            var missed = 0

            medicines.forEach {
                if (it.taken) {
                    taken++
                } else if (it.time < currentTime) {
                    missed++
                } else {
                    upcoming++
                }
            }

            tvTakenCount.text = taken.toString()
            tvUpcomingCount.text = upcoming.toString()
            tvMissedCount.text = missed.toString()

            recyclerToday.adapter = MedicineAdapter(medicines) { medicine ->
                lifecycleScope.launch {
                    db.medicineDao().updateTakenStatus(medicine.id, !medicine.taken)
                    loadTodayData()
                }
            }
        }
    }
}