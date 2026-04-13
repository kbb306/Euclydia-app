package com.example.euclydia.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.euclydia.R
import com.google.android.material.button.MaterialButton

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
                listener.onDialogPositiveClick(this)
            }
        }

        if (negative != null) {
            builder.setNegativeButton(negative) { _, _ ->
                dismiss()
            }
        }

        if (neutral != null) {
            builder.setNeutralButton(neutral) { _, _ ->
                listener.onDialogNeutralClick(this)
            }
        }

        return builder.create()
    }

    internal lateinit var listener : universalListener

    interface universalListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNeutralClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as universalListener
        } catch (e : ClassCastException) {
            throw ClassCastException(context.toString() + " must implement the interface")
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? AlertDialog
        dialog?.let {
            it.getButton(AlertDialog.BUTTON_POSITIVE)?.let {button ->
                if(button is MaterialButton) {
                    button.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background))
                    button.strokeWidth = (3 * resources.displayMetrics.density).toInt()
                    button.setStrokeColorResource(R.color.blue)
                    button.cornerRadius = (0 * resources.displayMetrics.density).toInt()
                }
            }
            it.getButton(AlertDialog.BUTTON_NEUTRAL).let {button ->
                if(button is MaterialButton) {
                    button.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background))
                    button.strokeWidth = (3 * resources.displayMetrics.density).toInt()
                    button.setStrokeColorResource(R.color.yellow)
                    button.cornerRadius = (0 * resources.displayMetrics.density).toInt()
                }
            }
            it.getButton(AlertDialog.BUTTON_NEGATIVE).let {button ->
                if(button is MaterialButton) {
                    button.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background))
                    button.strokeWidth = (3 * resources.displayMetrics.density).toInt()
                    button.setStrokeColorResource(R.color.red)
                    button.cornerRadius = (0 * resources.displayMetrics.density).toInt()
                }
            }
        }
    }
}