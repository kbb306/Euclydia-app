package com.example.euclydia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.euclydia.databinding.FollowFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import java.util.UUID

class FollowFragment(val uuid : UUID) : Fragment(), UniversalDialog.universalListener {
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
        binding.name.text = viewModel.followedName.toString()
        binding.xVal.text = viewModel.followedX.toString()
        binding.yVal.text = viewModel.followedY.toString()
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

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.unfollow()
        viewModel.delete(listOf(uuid))
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // N/A
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

} // Still not actually universal