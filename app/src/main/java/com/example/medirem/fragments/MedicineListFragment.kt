package com.example.medirem.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
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
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val db = MediRemDatabase.getDatabase(requireContext())
            val allMedicines = db.medicineDao().getAllMedicines()
            
            val adapter = MedicineAdapter(allMedicines) { medicine ->
                val bundle = Bundle().apply {
                    putInt("medicineId", medicine.id)
                }
                findNavController().navigate(R.id.action_medicineListFragment_to_medicineDetailsFragment, bundle)
            }
            recyclerView.adapter = adapter

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().lowercase()
                    val filtered = allMedicines.filter { 
                        it.name.lowercase().contains(query) || it.dosage.lowercase().contains(query)
                    }
                    recyclerView.adapter = MedicineAdapter(filtered) { medicine ->
                        val bundle = Bundle().apply {
                            putInt("medicineId", medicine.id)
                        }
                        findNavController().navigate(R.id.action_medicineListFragment_to_medicineDetailsFragment, bundle)
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_medicineListFragment_to_addMedicineFragment)
        }
    }
}