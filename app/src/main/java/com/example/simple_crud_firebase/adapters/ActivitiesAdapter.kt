package com.example.simple_crud_firebase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_crud_firebase.R
import com.example.simple_crud_firebase.models.Activities

class ActivitiesAdapter(private val activitiesList: ArrayList<Activities>) : RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {
    private var onClickListener: ActivitiesAdapter.OnClickListener? = null
    private var onLongClickListener : ActivitiesAdapter.OnLongClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
       return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivitiesAdapter.ViewHolder, position: Int) {
        val currentActivities = activitiesList[position]
        holder.tvActivitiesName.text = currentActivities.name
        holder.tvActivitiesCategory.text = currentActivities.category
        holder.tvActivitiesTime.text = currentActivities.time
        holder.itemView.setOnClickListener{
            if(onClickListener != null) {
                onClickListener!!.onClick(position,currentActivities)
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener!!.onLongClick(position,currentActivities)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvActivitiesName : TextView = itemView.findViewById(R.id.name)
        val tvActivitiesCategory : TextView = itemView.findViewById(R.id.category)
        val tvActivitiesTime : TextView = itemView.findViewById(R.id.time_activity)

    }

    interface OnClickListener{
        fun onClick(position: Int, model : Activities)
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int, model: Activities)
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener) {
        this.onLongClickListener = onLongClickListener
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


}