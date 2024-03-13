package com.example.happyehealthy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast


class CreateGoalsActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editHabit: EditText
    private lateinit var spinnerFrequency: Spinner
    private lateinit var buttonAddHabit: Button
    private lateinit var buttonTrackGoals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goals)

        editTextName = findViewById(R.id.editTextName)
        editHabit = findViewById(R.id.editTextHabit)
        spinnerFrequency = findViewById(R.id.spinnerFrequency)
        buttonAddHabit = findViewById(R.id.buttonAddHabit)
        buttonTrackGoals = findViewById(R.id.buttonTrackGoals)


        // Populate spinner with frequencies
//        val frequencies = arrayOf("Daily", "Weekly", "Monthly")


        buttonAddHabit.setOnClickListener {
            // Create a new EditText for the habit
            val newEditTextHabit = EditText(this)
            newEditTextHabit.layoutParams = editHabit.layoutParams
            newEditTextHabit.hint = getString(R.string.enter_habit)

            // Create a new Spinner for the frequency
            val newSpinnerFrequency = Spinner(this)
            val frequencies = resources.getStringArray(R.array.frequencies)
            newSpinnerFrequency.layoutParams = spinnerFrequency.layoutParams
            val frequencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencies)
            frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            newSpinnerFrequency.adapter = frequencyAdapter

            // Add the new views to the layout container
            findViewById<LinearLayout>(R.id.parentLayout).addView(newEditTextHabit)
            findViewById<LinearLayout>(R.id.parentLayout).addView(newSpinnerFrequency)
        }

        buttonTrackGoals.setOnClickListener {
            // Add functionality to track goals
            // Here, you can add code to navigate to the tracking goals activity
            // For simplicity, I'm showing a toast message
            Toast.makeText(this, "Track Goals Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
