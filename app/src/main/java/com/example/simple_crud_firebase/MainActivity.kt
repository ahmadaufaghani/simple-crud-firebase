package com.example.simple_crud_firebase

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_crud_firebase.adapters.ActivitiesAdapter
import com.example.simple_crud_firebase.models.Activities
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var faBtn : FloatingActionButton
    private lateinit var rvActivities: RecyclerView
    private lateinit var activitiesList : ArrayList<Activities>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseRef = FirebaseDatabase.getInstance().getReference("test")

        faBtn = findViewById(R.id.faBtn)
        faBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }
        rvActivities = findViewById(R.id.rvActivities)
        rvActivities.layoutManager = LinearLayoutManager(this)
        rvActivities.setHasFixedSize(true)

        activitiesList = arrayListOf<Activities>()
        getDataFromDB()


    }

    private fun getDataFromDB() {
        firebaseRef = FirebaseDatabase.getInstance().getReference("activities")

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activitiesList.clear()
                if(snapshot.exists()) {
                    for(activitiesSnap in snapshot.children) {
                        val activitiesData = activitiesSnap.getValue(Activities::class.java)
                        activitiesList.add(activitiesData!!)
                    }
                    val activitiesAdapter = ActivitiesAdapter(activitiesList)
                    rvActivities.adapter = activitiesAdapter

                    activitiesAdapter.setOnClickListener(object: ActivitiesAdapter.OnClickListener {
                        override fun onClick(position: Int, model: Activities) {
                            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                            intent.putExtra("EXTRA_ID",model.id)
                            intent.putExtra("EXTRA_NAME",model.name)
                            intent.putExtra("EXTRA_CATEGORY",model.category)
                            intent.putExtra("EXTRA_TIME",model.time)
                            startActivity(intent)
                        }

                    })

                    activitiesAdapter.setOnLongClickListener(object: ActivitiesAdapter.OnLongClickListener {
                        override fun onLongClick(position: Int, model: Activities) {
                            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                            alertDialogBuilder.setTitle("Delete Activity")
                            alertDialogBuilder.setMessage("Are you sure want to delete ${model.name}?")
                            alertDialogBuilder.setPositiveButton("Yes")
                            {_,_ ->
                                val firebaseRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("activities")
                                    firebaseRef.child(model.id.toString()).removeValue().addOnCompleteListener{
                                       Toast.makeText(applicationContext, "Activity successfully deleted!",Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener{
                                       Toast.makeText(applicationContext, "Error ${it.message}",Toast.LENGTH_SHORT).show()
                                   }
                            }
                            alertDialogBuilder.setNegativeButton("No"){_,_->
                                Toast.makeText(applicationContext, "Delete action cancelled!",Toast.LENGTH_SHORT).show()
                            }
                            val alertDialogBox = alertDialogBuilder.create()
                            alertDialogBox.show()
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}