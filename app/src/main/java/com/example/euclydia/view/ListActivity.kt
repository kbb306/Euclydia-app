package com.example.euclydia.view

import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euclydia.model.Shape
import kotlin.getValue


class ListActivity : AppCompatActivity() {
    private val viewModel: EuclydiaViewModel by viewModels()
    var delList : MutableList<Shape> = emptyList<Shape>() as MutableList<Shape> //Somehow add data from recyclerview here
}