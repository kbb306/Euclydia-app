package com.example.euclydia.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euclydia.R
import com.example.euclydia.databinding.CreateActivityBinding
import com.example.euclydia.model.Age
import com.example.euclydia.model.Gender
import com.example.euclydia.viewmodel.ColorOption
import kotlinx.serialization.InternalSerializationApi
import kotlin.getValue




class CreateActivity : AppCompatActivity() {
    private lateinit var binding: CreateActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()

    @OptIn(InternalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        val colorOptions = listOf(
            ColorOption("Red", Color.RED),
            ColorOption("Blue", android.graphics.Color.BLUE),
            ColorOption("Green", android.graphics.Color.GREEN),
            ColorOption("Yellow", android.graphics.Color.YELLOW),
            ColorOption("Cyan", android.graphics.Color.CYAN),
            ColorOption("Magenta", android.graphics.Color.MAGENTA),
            ColorOption("White", android.graphics.Color.WHITE),
            ColorOption("Black", android.graphics.Color.BLACK)
        )

        super.onCreate(savedInstanceState)
        binding = CreateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.zygote.name = p0.toString()
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }
        })

        binding.sideField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.zygote.sides = p0.toString().toIntOrNull() ?: 3
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }
        })

        binding.adult.isChecked = true
        binding.male.isChecked = true

        viewModel.zygote.age = Age.ADULT
        viewModel.zygote.gender = Gender.MALE

        binding.agegroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.zygote.age = when (checkedId) {
                R.id.adult -> Age.ADULT
                R.id.child -> Age.CHILD
                else -> viewModel.zygote.age
            }
        }

        binding.gendergroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.zygote.gender = when (checkedId) {
                R.id.male -> Gender.MALE
                R.id.female -> Gender.FEMALE
                R.id.nb -> Gender.ANDROGYNOUS
                else -> viewModel.zygote.gender
            }
        }

        val colorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            colorOptions.map { it.name }
        )

        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.color.adapter = colorAdapter

        binding.color.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                viewModel.zygote.color = colorOptions[p2].value
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.lengthfield.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.zygote.length = (p0.toString().toDoubleOrNull() ?: 3.33)
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }
        })

        binding.create.setOnClickListener {
            viewModel.create(viewModel.zygote)
            onBackPressedDispatcher.onBackPressed()
        }
    }

}
