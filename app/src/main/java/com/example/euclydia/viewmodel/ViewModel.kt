package com.example.euclydia.viewmodel

import androidx.lifecycle.ViewModel
import com.example.euclydia.model.*


class EuclydiaViewModel : ViewModel() {
    private var shapeList : MutableList<Shape> = mutableListOf()
    private var microphone = Speech()

    fun create(name : String, age: Age, gender: Gender, color : Int, sides : Int, // Raw data create(), may be deprecated soon?
              length : Double, x : Double, y : Double, heading : Double, speed :
               Double, lines : MutableList<String>) {
        val newShape  = Shape(name,age,gender,color,sides,length,x,y,heading,speed,lines)
        shapeList.add(newShape)
    }

    fun create(dna: DNA) { // used for import() and possibly standard shape creation
        val newShape = Shape(dna)
        shapeList.add(newShape)
    }

    fun create(dna : List<Any>) { // Used for  legacy import()
        val newShape  = Shape(dna)
        shapeList.add(newShape)
    }
}