package com.example.euclydia.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class UniversalDialog(
    private val title: String,
    private val message: String,
    private val positive: String?,
    private val negative: String?,
    private val neutral: String?
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(title)
            .setMessage(message)

        if (positive != null) {
            builder.setPositiveButton(positive) { _, _ ->
                // Callback function here
            }
        }

        if (negative != null) {
            builder.setNegativeButton(negative) { _, _ ->
                // Callback function here
            }
        }

        if (neutral != null) {
            builder.setNeutralButton(neutral) { _, _ ->
                // Callback function here
            }
        }

        return builder.create()
    }
}