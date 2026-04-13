package com.example.euclydia.view

import android.os.Bundle
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.viewmodel.ShapeAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ListActivity : AppCompatActivity() {
    private lateinit var binding: ListActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                // Intent here
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
                    binding = ListActivityBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    val shapes = viewModel.shapeList


                    binding.listView.adapter = adapter
                    binding.listView.layoutManager = LinearLayoutManager(
                        this
                    )
                    binding.back.setOnClickListener {
                        TODO("On stop call")
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
                    }
                }
            }