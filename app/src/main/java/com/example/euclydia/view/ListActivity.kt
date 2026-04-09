package com.example.euclydia.view

import android.os.Bundle
import android.os.PersistableBundle
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.model.Shape
import kotlin.getValue


class ListActivity : AppCompatActivity() {
    private lateinit var binding: ListActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()
    var select_List : MutableList<Shape> = emptyList<Shape>() as MutableList<Shape> //Somehow add data from recyclerview here
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            TODO("On stop call")
        }

        binding.selall.setOnClickListener {
            TODO("Add all to list")
        }

        binding.delete.setOnClickListener {
            TODO("Dialog fragment")
        }
    }
}