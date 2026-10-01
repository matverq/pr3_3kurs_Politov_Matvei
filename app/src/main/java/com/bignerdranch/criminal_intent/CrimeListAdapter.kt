package com.bignerdranch.criminal_intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClick: (Crime) -> Unit
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.crime_title)
        val dateTextView: TextView = view.findViewById(R.id.crime_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.titleTextView.text = crime.title

        val df = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        holder.dateTextView.text = df.format(crime.date)

        holder.itemView.setOnClickListener { onCrimeClick(crime) }
    }

    override fun getItemCount(): Int = crimes.size
}