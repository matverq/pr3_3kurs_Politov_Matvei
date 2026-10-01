package com.bignerdranch.criminal_intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.crime_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = CrimeListAdapter(CrimeLab.getCrimes()) { crime ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CrimeFragment.newInstance(crime.id))
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}