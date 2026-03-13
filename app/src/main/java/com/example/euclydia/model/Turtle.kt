package com.example.euclydia.model

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.compareTo

abstract class Turtle (
    val name : String,
    var x : Double,
    var y : Double,
    heading : Double,
    var speed : Double,
    val color : Int,
) {
    var isFollowed : Boolean = false
    var heading : Double = heading
        set(value) {
            field = normalize(value)
        }

    private fun normalize(angle: Double): Double {
        var a = angle % 360.0
        if (a < 0) a += 360.0
        return a
    }

    open fun update(worldHeight : Double ,worldWidth : Double) {
        forward(speed)
        wrap(worldWidth,worldHeight)
    }

    fun forward(distance : Double) {
        val radians = Math.toRadians(heading)
        x += (kotlin.math.cos(radians) * distance)
        y += (kotlin.math.sin(radians)* distance)
    }

    fun back(distance: Double) {
        val radians = Math.toRadians(heading)
        x -= (kotlin.math.cos(radians)*distance)
        y -= (kotlin.math.sin(radians)*distance)
    }

    fun goto(x : Double, y : Double) {
        this.x = x
        this.y = y
    }

    private fun left(degrees: Double) {
        heading += degrees
    }
    private fun right(degrees: Double) {
        heading -= degrees
    }

    fun turnTo(target : Double) {
            val target = normalize(target)
            val left = normalize(target  - heading)
            val right = normalize(heading - target)
            if (left <= right) {
                left(left)
            } else {
                right(right)
            }
        }
    private fun wrap(worldWidth: Double, worldHeight: Double) {
        x = when {
            x < 0f.toDouble() -> x + worldWidth
            x > worldWidth -> x - worldWidth
            else -> x
        }

        y = when {
            y < 0f.toDouble() -> y+ worldHeight
            y > worldHeight -> y- worldHeight
            else -> y
        }
    }

    fun avoid(other : Turtle) : Double { // Calculate a safe heading
        val dx = x - other.x
        val dy = y - other.y
        return Math.toDegrees(kotlin.math.atan2(dy,dx))
    }

    abstract fun draw(canvas: Canvas, paint: Paint, cameraX: Double, cameraY: Double)
}



