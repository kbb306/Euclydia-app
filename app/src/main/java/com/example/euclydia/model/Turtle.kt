package com.example.euclydia.model

import android.graphics.Canvas
import android.graphics.Paint

abstract class Turtle (
    var name : String,
    var x : Double,
    var y : Double,
    var heading : Double,
    var speed : Double,
    var color : Int,
) {
    var isFollowed : Boolean = false

    open fun update(worldHeight : Double ,worldWidth : Double) {
        forward(speed)
        wrap(worldWidth,worldHeight)
    }

    fun forward(distance : Double) {
        val radians = Math.toRadians(heading)
        x += (kotlin.math.cos(radians) * distance)
        y += (kotlin.math.sin(radians)* distance)
    }

    fun turn(degrees : Double) {
        heading = when {
            heading < 360 -> heading + degrees
            else -> heading - degrees
        } as Double
    }

    private fun wrap(worldWidth: Double, worldHeight: Double) {
        x = when {
            x < 0f.toDouble() -> x+= worldWidth
            x > worldWidth -> x-= worldWidth
            else -> x
        } as Double

        y = when {
            y < 0f.toDouble() -> y+= worldHeight
            y > worldHeight -> y-= worldHeight
            else -> y
        } as Double
    }

    abstract fun draw(canvas: Canvas, paint: Paint, cameraX: Double, cameraY: Double)
}
