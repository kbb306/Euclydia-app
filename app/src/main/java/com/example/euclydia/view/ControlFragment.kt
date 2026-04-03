package com.example.euclydia.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel

class ControlFragment() : Fragment() {
    private val viewModel: EuclydiaViewModel by activityViewModels()
    private lateinit var binding : ControlFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ControlFragmentBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.create.setOnClickListener {
            startActivity(Intent(requireContext(), CreateActivity::class.java))
        }

        binding.list.setOnClickListener {
            startActivity(Intent(requireContext(), ListActivity::class.java))
        }
    }
}