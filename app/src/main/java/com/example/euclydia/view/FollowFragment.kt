package com.example.euclydia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.euclydia.databinding.FollowFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import com.example.euclydia.viewmodel.LineAdapter
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.Locale

class FollowFragment(val uuid : UUID) : Fragment(), UniversalDialog.universalListener {
    private lateinit var binding : FollowFragmentBinding
    private val viewModel : EuclydiaViewModel by activityViewModels()
    private val adapter = LineAdapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lineview.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tick.collect {
                    binding.name.text = viewModel.followedName ?: ""
                    binding.xVal.text = viewModel.followedX?.let {
                        String.format(Locale.US,"%.2f", it)
                    } ?: ""

                    binding.yVal.text = viewModel.followedY?.let {
                        String.format(Locale.US,"%.2f", it)
                    } ?: ""
                }
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.followedLineLog.collect { lines ->
                    adapter.submitLines(lines)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.delete.setOnClickListener {
            val delete = UniversalDialog(
                title = "Confirm Deletion",
                message = "Are you sure you want to delete the selected shapes?",
                positive = "Yes",
                negative = "No",
                neutral = null
            )
            delete.show(childFragmentManager,"FOLLOW_DELETE")
        }
        binding.closeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.unfollow()
        viewModel.delete(listOf(uuid))
        parentFragmentManager.popBackStack()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // N/A
    }



} // Still not actually universal