package com.example.euclydia.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euclydia.databinding.LineEntryBinding
import com.example.euclydia.model.Shape
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

class LineAdapter() : RecyclerView.Adapter<LineViewHolder>(){

    private var lines : List<LineLogEntry> = emptyList()

    fun submitLines(newLines: List<LineLogEntry>) {
        val oldSize = lines.size
        lines = newLines

        if (newLines.size > oldSize && newLines.take(oldSize) == lines.take(oldSize)) {
            notifyItemRangeInserted(oldSize, newLines.size - oldSize)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): LineViewHolder {
        val binding = LineEntryBinding.inflate(LayoutInflater.from(p0.context),p0,false)
        return LineViewHolder(binding)
    }

    override fun onBindViewHolder(
        p0: LineViewHolder,
        p1: Int
    ) {
        p0.bind(lines[p1])
    }

    override fun getItemCount(): Int {
        return lines.size
    }

}

class LineViewHolder(val binding: LineEntryBinding,) : RecyclerView.ViewHolder(binding.root) {
    fun bind(linetext : LineLogEntry) {
        binding.apply {
            line.text = linetext.line
        }
    }

}