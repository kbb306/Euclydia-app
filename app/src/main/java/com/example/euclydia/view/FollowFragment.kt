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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euclydia.R
import com.example.euclydia.databinding.FollowFragmentBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import com.example.euclydia.viewmodel.LineAdapter
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

class FollowFragment : Fragment(), UniversalDialog.universalListener {
    companion object {
        private const val ARG_UUID = "uuid"

        fun newInstance(uuid: UUID): FollowFragment {
            return FollowFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_UUID, uuid)
                }
            }
        }
    }

    private var _binding: FollowFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EuclydiaViewModel by activityViewModels()
    private val adapter = LineAdapter()

    private val followedUuid: UUID?
        get() = arguments?.getSerializable(ARG_UUID, UUID::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = followedUuid
        if (id != null && viewModel.followedUUID.value != id) {
            viewModel.follow(id)
        }

        binding.lineview.layoutManager = LinearLayoutManager(requireContext())
        binding.lineview.adapter = adapter

        binding.delete.setOnClickListener {
            val delete = UniversalDialog(
                title = "Confirm Deletion",
                message = "Are you sure you want to delete the selected shape?",
                positive = "Yes",
                negative = "No",
                neutral = null
            )
            delete.show(childFragmentManager, "FOLLOW_DELETE")
        }

        binding.closeButton.setOnClickListener {
            viewModel.unfollow()
            parentFragmentManager.beginTransaction()
                .replace(R.id.bottom, ControlFragment())
                .commit()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tick.collect {
                    binding.name.text = viewModel.followedName ?: ""
                    binding.xValue.text = viewModel.followedX?.let {
                        String.format(Locale.US, "%.2f", it)
                    } ?: ""
                    binding.yValue.text = viewModel.followedY?.let {
                        String.format(Locale.US, "%.2f", it)
                    } ?: ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.followedLineLog.collect { lines ->
                    adapter.submitLines(lines)
                    if (lines.isNotEmpty()) {
                        binding.lineview.scrollToPosition(lines.lastIndex)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.lineview.adapter = null
        _binding = null
        super.onDestroyView()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val id = followedUuid ?: return
        viewModel.unfollow()
        viewModel.delete(listOf(id))
        parentFragmentManager.beginTransaction()
            .replace(R.id.bottom, ControlFragment())
            .commit()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {}
}