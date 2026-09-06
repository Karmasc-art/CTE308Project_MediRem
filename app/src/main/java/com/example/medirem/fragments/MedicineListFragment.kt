package com.example.medirem.fragments

import android.os.Bundle
import android.view.View
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

class MedicineListFragment : Fragment(R.layout.fragment_medicine_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMedicines)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val medicines = db.medicineDao().getAllMedicines()
            recyclerView.adapter = MedicineAdapter(medicines) { medicine ->
                val bundle = Bundle().apply {
                    putInt("medicineId", medicine.id)
                }
                findNavController().navigate(R.id.medicineDetailsFragment, bundle)
            }
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.addMedicineFragment)
        }
    }
}