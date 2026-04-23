package com.example.euclydia.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private lateinit var listener : Tracker

    fun setListener(listener : Tracker) {
        this.listener = listener
    }

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
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return true

        val tapX = event.x.toDouble()
        val tapY = event.y.toDouble()

        for (shape in shapes.reversed()) {
            val dx = tapX - shape.x
            val dy = tapY - shape.y
            val dist2 = dx * dx + dy * dy

            if (dist2 <= shape.radius * shape.radius) {
                listener.onSelect(shape.uuid)
                return true
            }
        }

        return true
    }
}


class PlaneFragment : Fragment(), Plane.Tracker {
    private val viewModel: EuclydiaViewModel by activityViewModels()
    private lateinit var binding: PlaneFragmentBinding

    private var listener : Plane.Tracker? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? Plane.Tracker ?: throw IllegalStateException("Host activity must implement plane tracker")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaneFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.planeView.setListener(this)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tick.collect {
                    binding.planeView.submit(viewModel.shapeList.value)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startLoop()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopLoop()
    }

    override fun onSelect(uuid: UUID) {
        listener?.onSelect(uuid)
    }

    fun setListener(listener : Plane.Tracker) {
        this.listener = listener
    }
}

