package com.example.euclydia.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.euclydia.databinding.ControlFragmentBinding
import com.example.euclydia.databinding.PlaneFragmentBinding
import com.example.euclydia.model.Shape
import com.example.euclydia.viewmodel.EuclydiaViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.getValue

class Plane @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : View(context,attrs) {

    interface Tracker {
        fun onSelect(uuid: UUID)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shapes : List<Shape> = emptyList()

    fun submit(newShapes: List<Shape>) {
        shapes = newShapes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        for (shape in shapes) {
            shape.draw(canvas,paint,0.0,0.0)
        }
    }
}


class PlaneFragment : androidx.fragment.app.Fragment() {
    private val viewModel: EuclydiaViewModel by activityViewModels()
    private lateinit var binding: PlaneFragmentBinding

    private lateinit var listener : Plane.Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        val plane : Plane = Plane(context)
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shapeList.collect { shapelist ->
                    plane.submit(shapelist)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaneFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        TODO("Use callback functions and a listener attribute in Shape to make clicking a shape trigger the follow fragment")
    }
}

