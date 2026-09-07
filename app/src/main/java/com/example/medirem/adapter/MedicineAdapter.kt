package com.example.medirem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.medirem.R
import com.example.medirem.data.Medicine
import com.example.medirem.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

class MedicineAdapter(
    private val medicines: List<Medicine>,
    private val onClick: (Medicine) -> Unit
) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDosage: TextView = view.findViewById(R.id.tvDosage)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val cardStatus: CardView = view.findViewById(R.id.cardStatus)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
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
        holder.tvTime.text = TimeUtils.formatToAmPm(medicine.time)

        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        if (medicine.taken) {
            holder.tvStatus.text = "Taken"
            holder.cardStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.taken_green))
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.taken_text))
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.taken_text))
        } else if (medicine.date < todayDate || (medicine.date == todayDate && medicine.time < currentTime)) {
            holder.tvStatus.text = "Missed"
            holder.cardStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.missed_red))
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.missed_text))
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.missed_text))
        } else {
            holder.tvStatus.text = "Upcoming"
            holder.cardStatus.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.upcoming_yellow))
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.upcoming_text))
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.upcoming_text))
        }

        holder.itemView.setOnClickListener { onClick(medicine) }
    }

    override fun getItemCount() = medicines.size
}