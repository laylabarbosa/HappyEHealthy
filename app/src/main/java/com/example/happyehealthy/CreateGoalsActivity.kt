package com.example.happyehealthy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.happyehealthy.R


class CreateGoalsActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var spinnerHabit: Spinner
    private lateinit var spinnerFrequency: Spinner
    private lateinit var buttonAddHabit: Button
    private lateinit var buttonTrackGoals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goals)

        editTextName = findViewById(R.id.editTextName)
        spinnerHabit = findViewById(R.id.spinnerHabit)
        spinnerFrequency = findViewById(R.id.spinnerFrequency)
        buttonAddHabit = findViewById(R.id.buttonAddHabit)
        buttonTrackGoals = findViewById(R.id.buttonTrackGoals)

        // Populate spinner with habits
        val habits = arrayOf("Exercise", "Read", "Meditate", "Eat Healthily")
        val habitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, habits)
        habitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHabit.adapter = habitAdapter

        // Populate spinner with frequencies
        val frequencies = arrayOf("Daily", "Weekly", "Monthly")
        val frequencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencies)
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrequency.adapter = frequencyAdapter

        buttonAddHabit.setOnClickListener {
            // Add functionality to add a new habit and frequency dynamically
            // Here, you can add code to create new UI elements for adding habits and frequencies
            // For simplicity, I'm showing a toast message
            Toast.makeText(this, "Add Habit Button Clicked", Toast.LENGTH_SHORT).show()
        }

        buttonTrackGoals.setOnClickListener {
            // Add functionality to track goals
            // Here, you can add code to navigate to the tracking goals activity
            // For simplicity, I'm showing a toast message
            Toast.makeText(this, "Track Goals Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
