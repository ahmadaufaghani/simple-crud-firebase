package com.example.simple_crud_firebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.simple_crud_firebase.models.Activities
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var updateBtn : Button
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var btnBack : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)


        val name = intent.getStringExtra("EXTRA_NAME")
        val category = intent.getStringExtra("EXTRA_CATEGORY")
        val time = intent.getStringExtra("EXTRA_TIME")

        val editTextName = findViewById<TextView>(R.id.editTextNameUpdate)
        val editTextCategory = findViewById<TextView>(R.id.editTextCategoryUpdate)
        val editTextTime = findViewById<TextView>(R.id.editTextTimeUpdate)

        editTextName.text = name
        editTextCategory.text = category
        editTextTime.text = time

        updateBtn = findViewById(R.id.btnUpdateActivity)
        btnBack = findViewById(R.id.btnBack)
        updateBtn.setOnClickListener {
            updateData()
        }
        btnBack.setOnClickListener{
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateData() {
        val editTextName = findViewById<TextView>(R.id.editTextNameUpdate)
        val editTextCategory = findViewById<TextView>(R.id.editTextCategoryUpdate)
        val editTextTime = findViewById<TextView>(R.id.editTextTimeUpdate)

        val id = intent.getStringExtra("EXTRA_ID").toString()
        val name = editTextName.text.toString()
        val category = editTextCategory.text.toString()
        val time = editTextTime.text.toString()

        val activities = Activities(id, name, category, time)
        firebaseRef = FirebaseDatabase.getInstance().getReference("activities")
        firebaseRef.child(id).setValue(activities).addOnCompleteListener{
            startActivity(Intent(this@UpdateActivity, MainActivity::class.java))
            Toast.makeText(applicationContext, "Activity successfully updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            startActivity(Intent(this@UpdateActivity, MainActivity::class.java))
            Toast.makeText(applicationContext, "Error ${it.message}", Toast.LENGTH_SHORT).show()
        }


    }


}