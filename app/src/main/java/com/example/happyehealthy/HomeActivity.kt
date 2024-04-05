package com.example.happyehealthy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
//defining variables
    private lateinit var textGreeting: TextView
    private lateinit var textDateTime: TextView
    private lateinit var habitsLayout: LinearLayout
    private lateinit var btnLogout: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initializing Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Initializing views
        textGreeting = findViewById(R.id.textGreeting)
        textDateTime = findViewById(R.id.textDateTime)
        habitsLayout = findViewById(R.id.habitsLayout)
        btnLogout = findViewById(R.id.btn_logout)
        val btnSteps = findViewById<Button>(R.id.btn_steps)

        // calling method to write date and time
        updateDateTime()

        // calling method to load user's info from from database
        loadHabitsFromDatabase()

        // Logout button click listener
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnSteps.setOnClickListener {
            startActivity(Intent(this, StepsActivity::class.java))
        }

    }

    private fun updateDateTime() {
        // Getting current date and time
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EE MM dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateTimeString = dateFormat.format(calendar.time)
        val timeString = timeFormat.format(calendar.time)

        // Updating textDateTime TextView
        val dateTimeTextView = "$dateTimeString\n$timeString"
        textDateTime.text = dateTimeTextView
    }

    private fun loadHabitsFromDatabase() {
        val currentUser = auth.currentUser
        val email = currentUser?.email
        if (email != null) {
            val emailKey = email.replace(".", ",")

            // Get reference to user's data in the database using the email as the key
            val goalsRef = databaseReference.child("users").child(emailKey)

            // Listen for changes in the goals data
            goalsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Clear previous habit items
                    habitsLayout.removeAllViews()

                    // Iterate through each child under the emailKey
                    dataSnapshot.children.forEach { userSnapshot ->
                        // Getting the username
                        val userName = userSnapshot.key.toString()

                        // Setting the greeting dynamically
                        val calendar = Calendar.getInstance()
                        val timeGreeting = when (calendar.get(Calendar.HOUR_OF_DAY)) {
                            in 0..11 -> "Good Morning!"
                            in 12..15 -> "Good Afternoon!"
                            in 16..20 -> "Good Evening!"
                            else -> "Good Night!"
                        }
                        textGreeting.text = "Hi $userName, $timeGreeting"

                        // Iterate through each habit under the user's data
                        userSnapshot.children.forEach { habitSnapshot ->
                            // Get the habit name and capitalize if necessary
                            val habitName = habitSnapshot.child("first").value.toString()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                            // Create a horizontal LinearLayout to hold the habit name and toggle button
                            val horizontalLayout = LinearLayout(this@HomeActivity)
                            horizontalLayout.orientation = LinearLayout.HORIZONTAL

                            // Create a TextView for the habit name
                            val habitTextView = TextView(this@HomeActivity)
                            habitTextView.text = habitName
                            habitTextView.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                            //making the habit font look nicer
                            habitTextView.textSize = 24f
                            habitTextView.setShadowLayer(0f, 10f, 10f, Color.BLACK)

                            // Create a toggle button for the habit
                            val toggleButton = androidx.appcompat.widget.AppCompatToggleButton(this@HomeActivity)
                            toggleButton.textOff = "Complete"
                            toggleButton.textOn = "Complete"
                            toggleButton.isChecked = false // Ensure toggle starts unchecked
                            toggleButton.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )

                            // Add click listener to handle habit selection
                            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    // Habit is selected
                                    toggleButton.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, android.R.color.holo_green_light))
                                    Toast.makeText(this@HomeActivity, "Congratulations, $habitName is done!", Toast.LENGTH_SHORT).show()
                                } else {
                                    toggleButton.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, android.R.color.transparent))
                                    Toast.makeText(this@HomeActivity, "You still have time to: $habitName", Toast.LENGTH_SHORT).show()
                                }
                            }

                            // Add the TextView and toggle button to the horizontal layout
                            horizontalLayout.addView(habitTextView)
                            horizontalLayout.addView(toggleButton)

                            // Add the horizontal layout to habitsLayout
                            habitsLayout.addView(horizontalLayout)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Show error message with Toast
                    Toast.makeText(this@HomeActivity, "Error loading habits: ${databaseError.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }


}
