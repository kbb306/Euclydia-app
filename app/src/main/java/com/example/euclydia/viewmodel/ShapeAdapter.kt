package com.example.euclydia.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euclydia.databinding.ListElementBinding
import com.example.euclydia.model.Shape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class ShapeAdapter(private var shapelist : List<Shape>,
                   private val ids : MutableSet<UUID>,
                   private val onCheckedChange: (Shape, Boolean) -> Unit
) : RecyclerView.Adapter<ShapeListViewHolder>() {
    private var onClickListener : View.OnClickListener? = null

    fun updateShapes(newShapes: List<Shape>) {
        shapelist = newShapes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ShapeListViewHolder {
        val binding = ListElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ShapeListViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ShapeListViewHolder, p1: Int) {
        val shape = shapelist[p1]
        val isChecked = ids.contains(shape.uuid)
        p0.bind(shape, isChecked, onCheckedChange)
    }

    override fun getItemCount(): Int {
        return shapelist.size
    }


}

class ShapeListViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shape : Shape,
             isChecked : Boolean,
             onCheckedChange: (Shape, Boolean) -> Unit) {
        binding.apply {
            shapeName.text = shape.name
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = isChecked
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(shape,isChecked)
            }
            shapeView.setImageDrawable(  TODO("Set icon to shape" ))

        }
    }

}