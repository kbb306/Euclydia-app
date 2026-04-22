package com.example.euclydia.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.euclydia.databinding.ControlFragmentBinding

class ControlFragment() : Fragment() {

    private var _binding : ControlFragmentBinding? = null
    private val binding get() =_binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ControlFragmentBinding.inflate(layoutInflater,container,false)

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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}