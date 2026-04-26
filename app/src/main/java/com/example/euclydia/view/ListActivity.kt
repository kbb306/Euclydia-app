package com.example.euclydia.view

import android.os.Bundle
import com.example.euclydia.viewmodel.EuclydiaViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euclydia.databinding.ListActivityBinding
import com.example.euclydia.viewmodel.ShapeAdapter
import kotlinx.coroutines.launch
import kotlin.getValue
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts


class ListActivity : AppCompatActivity(), UniversalDialog.universalListener {
    private lateinit var binding: ListActivityBinding
    private val viewModel: EuclydiaViewModel by viewModels()

    private val openImportFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri ?: return@registerForActivityResult

            val name = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && index >= 0) cursor.getString(index) else null
            } ?: uri.toString()

            try {
                when {
                    name.endsWith(".json", ignoreCase = true) -> {
                        viewModel.importJson(this, uri)
                        Toast.makeText(this, "Imported JSON shapes", Toast.LENGTH_SHORT).show()
                    }

                    name.endsWith(".csv", ignoreCase = true) -> {
                        viewModel.legacyImport(this, uri)
                        Toast.makeText(this, "Imported legacy shapes", Toast.LENGTH_SHORT).show()
                    }

                    name.endsWith(".xml", ignoreCase = true) -> {
                        Toast.makeText(this, "XML import is not implemented yet", Toast.LENGTH_LONG).show()
                    }

                    else -> {
                        Toast.makeText(this, "Choose a .json or .csv file", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Import failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    private val createExportFile =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri: Uri? ->
            uri ?: return@registerForActivityResult

            try {
                viewModel.exportJson(this, uri)
                Toast.makeText(this, "Export complete", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.list) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun createAdapter(): ShapeAdapter {
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
            return adapter
        }

        fun setAllChecked() {
            viewModel.select_ids.forEach { id ->

            }
        }

        var adapter = createAdapter()
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shapeList.collect { shapes ->
                    adapter.updateShapes(shapes)
                }
            }
        }

                    val shapes = viewModel.shapeList


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
                            adapter = createAdapter()
                            binding.listView.adapter = adapter

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

                    binding.importbutton.setOnClickListener {
                        openImportFile.launch(
                            arrayOf(
                                "application/json",
                                "text/csv",
                                "text/comma-separated-values",
                                "text/xml",
                                "application/xml",
                                "*/*"
                            )
                        )
                    }

                    binding.exportbutton.setOnClickListener {
                        createExportFile.launch("euclydia-shapes.json")
                    }
                            }




    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.delete(viewModel.select_ids.toList())
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // N/A
    }


    // This isn't as universal as I would have liked.
}