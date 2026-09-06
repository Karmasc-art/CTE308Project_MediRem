package com.example.medirem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medirem.R
import com.example.medirem.data.Medicine

class MedicineAdapter(
    private val medicines: List<Medicine>,
    private val onClick: (Medicine) -> Unit
) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDosage: TextView = view.findViewById(R.id.tvDosage)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicines[position]
        holder.tvName.text = medicine.name
        holder.tvDosage.text = medicine.dosage
        holder.tvTime.text = medicine.time
        holder.itemView.setOnClickListener { onClick(medicine) }
    }

    override fun getItemCount() = medicines.size
}