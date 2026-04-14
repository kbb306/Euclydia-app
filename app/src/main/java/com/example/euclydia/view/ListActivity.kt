package com.example.euclydia.view

import android.content.Intent
import android.os.Bundle
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.viewmodel.ShapeAdapter
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.getValue


class ListActivity : AppCompatActivity(), UniversalDialog.universalListener {
    private lateinit var binding: ListActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ShapeAdapter(
            emptyList(),
            viewModel.select_ids,
            onCheckedChange = { shape, isChecked ->
                if (isChecked) {
                    viewModel.select_ids.add(shape.uuid)
                } else {
                    viewModel.select_ids.remove(shape.uuid)
                }
            },
            onFollowClick = { uuid ->
                val followCall = MainActivity.createIntent(this,uuid)
                startActivity(followCall)
            }
        )



        binding.listView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shapeList.collect { shapes ->
                    adapter.updateShapes(shapes)
                }
            }
        }

                    val shapes = viewModel.shapeList


                    binding.listView.adapter = adapter
                    binding.listView.layoutManager = LinearLayoutManager(
                        this
                    )
                    binding.back.setOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    binding.selall.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.select_ids.clear()
                            viewModel.select_ids.addAll(shapes.value.map { it.uuid })
                        } else {
                            viewModel.select_ids.clear()
                        }

                    }

                    binding.delete.setOnClickListener {
                        val delete = UniversalDialog(
                            title = "Confirm Deletion",
                            message = "Are you sure you want to delete the selected shapes?",
                            positive = "Yes",
                            negative = "No",
                            neutral = null
                        )
                        delete.show(supportFragmentManager,"LIST_DELETE")
                    }
                }

    override fun onStop() {
        viewModel.syncInator()
        super.onStop()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.delete(viewModel.select_ids.toList())
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // N/A
    }


    // This isn't as universal as I would have liked.
}