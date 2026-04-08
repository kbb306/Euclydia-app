package com.example.euclydia.viewmodel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.view.ListActivity

class ShapeAdaptor : RecyclerView.Adapter<ShapeListViewHolder>() {
    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ShapeListViewHolder {

        return TODO("Provide the return value")
    }

    override fun onBindViewHolder(p0: ShapeListViewHolder, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class ShapeListViewHolder(val binding: ListActivityBinding) : RecyclerView.ViewHolder(binding.root) {}