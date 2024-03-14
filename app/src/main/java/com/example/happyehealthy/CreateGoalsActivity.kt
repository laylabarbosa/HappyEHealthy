package com.example.happyehealthy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase


class CreateGoalsActivity : AppCompatActivity() {
    //defining variables
    private lateinit var editTextName: EditText
    private lateinit var editHabit: EditText
    private lateinit var spinnerFrequency: Spinner
    private lateinit var buttonAddHabit: Button
    private lateinit var buttonTrackGoals: Button
    private lateinit var btnLogout: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goals)
        //linking variables to xml items
        editTextName = findViewById(R.id.editTextName)
        editHabit = findViewById(R.id.editTextHabit)
        spinnerFrequency = findViewById(R.id.spinnerFrequency)
        buttonAddHabit = findViewById(R.id.buttonAddHabit)
        buttonTrackGoals = findViewById(R.id.buttonTrackGoals)
        btnLogout = findViewById(R.id.btn_logout)

        //creating more fiels for input based on user desire
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
        //sending habits information to database
        buttonTrackGoals.setOnClickListener {
            // Get the name from the EditText
            val name = editTextName.text.toString().trim()

            // Create a list to hold the habit and frequency data
            val habitsList = mutableListOf<Pair<String, String>>()

            // Iterate over the Layout to get the data from each habit and frequency pair
            val parentLayout = findViewById<LinearLayout>(R.id.parentLayout)
            for (i in 0 until parentLayout.childCount step 2) {
                val habitEditText = parentLayout.getChildAt(i) as EditText
                val frequencySpinner = parentLayout.getChildAt(i + 1) as Spinner

                val habit = habitEditText.text.toString().trim()
                val frequency = frequencySpinner.selectedItem.toString()

                habitsList.add(Pair(habit, frequency))
            }

            // Get the currently logged in user's email
            val email = MainActivity.auth.currentUser?.email

            // Convert the email to a valid Firebase key by replacing '.' with ','
            val emailKey = email!!.replace(".", ",")

            // Send the data to Firebase under the user's email
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(emailKey).child(name)
            databaseReference.setValue(habitsList)
                .addOnSuccessListener {
                    Toast.makeText(this, "Goals tracked successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CreateGoalsActivity, HomeActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to track goals: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        //longing out current user
        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity :: class.java))
            finish()
        }

    }
}
