package com.example.euclydia.view

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.*
import android.graphics.drawable.Drawable
import com.example.euclydia.model.Shape

class ShapeIcon(
    private val shape: Shape
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2f
        color = shape.color
    }

    override fun draw(canvas: Canvas) {
        val path = shape.drawPath(0.0,0.0)
        val pathBounds = RectF()
        path.computeBounds(pathBounds,true)

        if (pathBounds.isEmpty) return

        val drawableBounds = RectF(bounds)
        val matrix = Matrix()

        val padding = 4f
        val target = RectF(
            drawableBounds.left + padding,
            drawableBounds.top + padding,
            drawableBounds.right - padding,
            drawableBounds.bottom - padding
        )

        val scale = minOf(
            target.width() / pathBounds.width(),
            target.height() / pathBounds.height()
        )

        matrix.postTranslate(-pathBounds.left, -pathBounds.top)
        matrix.postScale(scale,scale)

        val scaledWidth = pathBounds.width() * scale
        val scaledHeight = pathBounds.height() * scale
        val dx = target.left + (target.width() - scaledWidth) / 2f
        val dy = target.top + (target.height() - scaledHeight) / 2f
        matrix.postTranslate(dx, dy)

        path.transform(matrix)

        canvas.drawPath(path,paint)
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}