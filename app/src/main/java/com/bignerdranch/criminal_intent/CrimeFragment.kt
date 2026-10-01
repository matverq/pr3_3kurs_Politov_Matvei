package com.bignerdranch.criminal_intent

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import  android.net.Uri
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.widget.AppCompatButton
import java.util.UUID

class CrimeFragment : Fragment() {
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var solvedCheckBox: CheckBox
    private  lateinit var suspectButton: AppCompatButton
    private  lateinit var reportButton: AppCompatButton
    private lateinit var dateButton: AppCompatButton

    private val pickContactLauncher= registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { contactUri: Uri? ->
        contactUri?.let { uri ->
            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            val cursor = requireActivity().contentResolver.query(uri, queryFields, null, null, null)
            cursor?.use {
                if ((it.moveToFirst())) {
                    val suspectname = it.getString(0)
                    crime.suspect = suspectname
                    suspectButton.text = suspectname
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as? UUID
        crime = if (crimeId != null) {
            CrimeLab.getCrime(crimeId) ?: Crime()
        } else {
            Crime()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as AppCompatButton
        solvedCheckBox=view.findViewById(R.id.crime_solved) as CheckBox
        dateButton.apply {
            text=crime.date.toString()
            isEnabled=false
        }
        suspectButton = view.findViewById(R.id.crime_suspect) as AppCompatButton
        suspectButton.setOnClickListener {
            pickContactLauncher.launch(null)
        }

        reportButton = view.findViewById(R.id.crime_report) as AppCompatButton
        reportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, "Crime Report")
            }
            val chooserIntent = Intent.createChooser(intent, "Send report via")
            startActivity(chooserIntent)
        }
        return view
    }
    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
        }
    }
    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) "Solved" else "Not Solved"
        val df = SimpleDateFormat("EEE, MMM dd", Locale.US)
        val dateString = df.format(crime.date)

        val suspectString = if (crime.suspect.isBlank()) {
            "no suspect."
        } else {
            "suspect is ${crime.suspect}."
        }

        return "Title: ${crime.title}, Date: $dateString, Status: $solvedString, $suspectString"
    }
    companion object {
        private const val ARG_CRIME_ID = "crime_id"

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply { arguments = args }
        }
    }
}