package com.example.euclydia.view

import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlin.getValue


class ListActivity : AppCompatActivity() {
    private val viewModel: EuclydiaViewModel by viewModels()
}