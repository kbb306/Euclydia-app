package com.example.euclydia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.euclydia.databinding.FollowFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import java.util.UUID

class FollowFragment(val uuid : UUID) : Fragment() {
    private lateinit var binding : FollowFragmentBinding
    private val viewModel : EuclydiaViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.follow(uuid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FollowFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

    }

}