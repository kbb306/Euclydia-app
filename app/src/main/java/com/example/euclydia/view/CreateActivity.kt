package com.example.euclydia.view

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euclydia.R
import com.example.euclydia.databinding.CreateActivityBinding
import com.example.euclydia.model.Age
import com.example.euclydia.model.Gender
import kotlinx.serialization.InternalSerializationApi
import kotlin.getValue


class CreateActivity : AppCompatActivity() {
    private lateinit var binding: CreateActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()

    @OptIn(InternalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
                TODO("Not yet implemented")
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                TODO("Not yet implemented")
            }
        })

        binding.sideField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.zygote.sides = p0.toString().toInt()
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                TODO("Not yet implemented")
            }
        })

        viewModel.zygote.age = when(binding.agegroup.checkedRadioButtonId) {
            R.id.adult -> Age.ADULT
            R.id.child -> Age.CHILD
            else -> null
        }!!

        viewModel.zygote.gender = when(binding.gendergroup.checkedRadioButtonId) {
            R.id.male -> Gender.MALE
            R.id.female -> Gender.FEMALE
            R.id.nb -> Gender.ANDROGYNOUS
            else -> null
        }!!

        binding.color.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                viewModel.zygote.color = p0?.getItemAtPosition(p2).toString().toInt()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

}