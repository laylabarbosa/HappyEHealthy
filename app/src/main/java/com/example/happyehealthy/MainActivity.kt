package com.example.happyehealthy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.happyehealthy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Inside onCreate method of MainActivity or Application class



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        lateinit var auth: FirebaseAuth
        lateinit var database: FirebaseDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //connecting to the firebase
        auth= FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //validating user's credentials
        binding.btnLogin.setOnClickListener {
            val email = binding.edtxtEmail.text.toString()
            val password = binding.edtxtPass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                MainActivity.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = MainActivity.auth.currentUser
                        val email = currentUser?.email
                        if (email != null) {
                            // Convert the email to a valid Firebase key by replacing '.' with ','
                            val emailKey = email.replace(".", ",")

                            // Check if the user has a corresponding entry in the database for habits
                            MainActivity.database.getReference("users").child(emailKey).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // User has entry for habits, redirect to HomeActivity
                                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                    } else {
                                        // No entry for habits, redirect to CreateGoalsActivity
                                        startActivity(Intent(this@MainActivity, CreateGoalsActivity::class.java))
                                    }
                                    finish()
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Toast.makeText(this@MainActivity, "Failed to check habits: ${databaseError.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        //button for new users to create account
        binding.btnCreateAcc.setOnClickListener {
            startActivity(Intent(this, RegisterActivity :: class.java))
            finish()
        }

    }
}