package com.example.euclydia.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.databinding.ListElementBinding
import com.example.euclydia.model.Shape
import com.example.euclydia.view.ListActivity

class ShapeAdaptor(private val shapelist : List<Shape>) : RecyclerView.Adapter<ShapeListViewHolder>() {
    private var onClickListener : View.OnClickListener? = null
    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ShapeListViewHolder {
        val binding = ListElementBinding.inflate(LayoutInflater.from(parent.context))
        return ShapeListViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ShapeListViewHolder, p1: Int) {

    }

    override fun getItemCount(): Int {
        return shapelist.size
    }


}

class ShapeListViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shape : Shape) {
        binding.apply {
            shapeName.text = shape.name
            checkBox.setOnCheckedChangeListener { button, bool ->
                if (bool) {
                    TODO("How to add to wider list of deletables in ListElement")
                }

                else {

                }
            }
            shapeView.setImageDrawable(  TODO("Set icon to shape" ))

        }
    }

}