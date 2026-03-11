package com.example.euclydia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.euclydia.model.Age
import com.example.euclydia.model.DNA
import com.example.euclydia.model.Gender
import com.example.euclydia.model.Shape
import com.example.euclydia.model.ShapeJson
import com.example.euclydia.model.Speech
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.InternalSerializationApi


class EuclydiaViewModel : ViewModel() {
    private var shapeList : MutableList<Shape> = mutableListOf()
    private var microphone = Speech()

    fun create(name : String, age: Age, gender: Gender, color : Int, sides : Int, // Raw data create(), may be deprecated soon?
              length : Double, x : Double, y : Double, heading : Double, speed :
               Double, lines : MutableList<String>) {
        val newShape  = Shape(name,age,gender,color,sides,length,x,y,heading,speed,lines)
        shapeList.add(newShape)
    }

    @OptIn(InternalSerializationApi::class)
    fun create(dna: DNA) { // used for import() and possibly standard shape creation
        val newShape = Shape(dna)
        shapeList.add(newShape)
    }

    fun create(dna : List<Any>) { // Used for  legacy import()
        val newShape  = Shape(dna)
        shapeList.add(newShape)
    }

    fun legacyImport(context : Context, path: String) {
        csvReader().open(context.openFileInput(path)) {
            readAllAsSequence().forEach { row ->
                create(row)
            }
        }
    }

    fun import(context : Context,path: String) {
        val cryofreeze = context.openFileInput(path)
            .bufferedReader().use { it.readText() }
        shapeList.addAll(ShapeJson.decodeShapes(cryofreeze) as MutableList<Shape>)
    }

    fun export(context: Context, path: String) {
        val cryofreeze = ShapeJson.encodeShapes(shapeList)
        context.openFileOutput(path, Context.MODE_PRIVATE).use { it.write(cryofreeze.toByteArray()) }
    }

}