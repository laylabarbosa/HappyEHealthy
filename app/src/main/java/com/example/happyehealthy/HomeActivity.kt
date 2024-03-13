package com.example.happyehealthy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var textGreeting: TextView
    private lateinit var textDateTime: TextView
    private lateinit var habitsLayout: LinearLayout
    private lateinit var btnLogout: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Initialize views
        textGreeting = findViewById(R.id.textGreeting)
        textDateTime = findViewById(R.id.textDateTime)
        habitsLayout = findViewById(R.id.habitsLayout)
        btnLogout = findViewById(R.id.btn_logout)

        // Set greeting with user's name
        val currentUser = auth.currentUser
        val name = currentUser?.displayName ?: "User"
        textGreeting.text = "Hi $name, Good Morning!"

        // Update date and time
        updateDateTime()

        // Load habits from database
        loadHabitsFromDatabase()

        // Logout button click listener
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateDateTime() {
        // Get current date and time
        val dateTime = java.util.Calendar.getInstance().time.toString()

        // Update textDateTime TextView
        textDateTime.text = dateTime
    }

    private fun loadHabitsFromDatabase() {
        val currentUser = auth.currentUser
        val email = currentUser?.email
        if (email != null) {
            // Convert the email to a valid Firebase key by replacing '.' with ','
            val emailKey = email.replace(".", ",")

            // Get reference to user's goals in the database using the email as the key
            val goalsRef = databaseReference.child("users").child(emailKey)

            // Listen for changes in the goals data
            goalsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Clear previous habit items
                    habitsLayout.removeAllViews()

                    // Iterate through each goal (habit) under the goals node
                    dataSnapshot.children.forEach { habitSnapshot ->
                        // Get the habit name
                        val habitName = habitSnapshot.key.toString()

                        // Create a toggle button for the habit
                        val toggleButton = androidx.appcompat.widget.AppCompatToggleButton(this@HomeActivity)
                        toggleButton.text = habitName

                        // Add click listener to handle habit selection
                        toggleButton.setOnCheckedChangeListener { _, isChecked ->
                            // Handle habit selection here
                            if (isChecked) {
                                // Habit is selected
                                Toast.makeText(this@HomeActivity, "Habit selected: $habitName", Toast.LENGTH_SHORT).show()
                            } else {
                                // Habit is deselected
                                Toast.makeText(this@HomeActivity, "Habit deselected: $habitName", Toast.LENGTH_SHORT).show()
                            }
                        }

                        // Add the toggle button to habitsLayout
                        habitsLayout.addView(toggleButton)
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
