package com.example.simple_crud_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.simple_crud_firebase.models.Activities
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var btnAddActivity: Button
    private lateinit var btnBack : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        firebaseRef = FirebaseDatabase.getInstance().getReference("activities")
        btnAddActivity = findViewById(R.id.btnAddActivity)
        btnBack = findViewById(R.id.btnBack)
        btnAddActivity.setOnClickListener {
            saveData()
        }
        btnBack.setOnClickListener{
            val intent = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(intent)
        }



    }

    private fun saveData() {
        val editTextName = findViewById<TextView>(R.id.editTextName)
        val editTextCategory = findViewById<TextView>(R.id.editTextCategory)
        val editTextTime = findViewById<TextView>(R.id.editTextTime)

        val name = editTextName.text.toString()
        val category = editTextCategory.text.toString()
        val time = editTextTime.text.toString()

        if (name.isEmpty()) editTextName.error = "Enter the activity name, please!"
        if (category.isEmpty()) editTextName.error = "Enter the activity category, please!"
        if (time.isEmpty()) editTextName.error = "Enter the activity time, please!"

        val activityId = firebaseRef.push().key!!
        val activities = Activities(activityId, name, category, time)
        firebaseRef.child(activityId).setValue(activities).addOnCompleteListener {
            startActivity(Intent(this@AddActivity, MainActivity::class.java))
            Toast.makeText(applicationContext, "Activity successfully added!", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            startActivity(Intent(this@AddActivity, MainActivity::class.java))
            Toast.makeText(
                applicationContext, " Error : ${
                    it.message
                }", Toast.LENGTH_SHORT
            ).show()
        }
    }
}