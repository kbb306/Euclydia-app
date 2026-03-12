package com.example.euclydia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.euclydia.model.Age
import com.example.euclydia.model.DNA
import com.example.euclydia.model.Gender
import com.example.euclydia.model.Shape
import com.example.euclydia.model.ShapeJson
import com.example.euclydia.model.Speech
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.InternalSerializationApi
import java.util.UUID
import kotlin.uuid.Uuid


class EuclydiaViewModel(lifecycleScope: CoroutineScope) : ViewModel() {
    private val _shapes = MutableStateFlow<List<Shape>>(emptyList())
    val shapeList : StateFlow<List<Shape>> = _shapes.asStateFlow()

    private val _tick = MutableStateFlow(0L)
    val tick : StateFlow<Long> = _tick.asStateFlow()

    private var microphone = Speech(lifecycleScope)

    // Import/Export and dependencies
    fun create(name : String, age: Age, gender: Gender, color : Int, sides : Int, // Raw data create(), may be deprecated soon?
              length : Double, x : Double, y : Double, heading : Double, speed :
               Double, lines : MutableList<String>) {
        val newShape  = Shape(name,age,gender,color,sides,length,x,y,heading,speed,lines)
        _shapes.value += newShape
    }

    @OptIn(InternalSerializationApi::class)
    fun create(dna: DNA) { // used for import() and possibly standard shape creation
        val newShape = Shape(dna)
        _shapes.value += newShape
    }


    fun legacyImport(context : Context, path: String) {
        val loaded = mutableListOf<Shape>()
        csvReader().open(context.openFileInput(path)) {
            readAllAsSequence().forEach { row ->
                loaded.add(Shape(row as List<Any>))
            }
        }
        _shapes.value += loaded
    }

    fun import(context : Context,path: String) {
        val cryofreeze = context.openFileInput(path)
            .bufferedReader().use { it.readText() }
        _shapes.value += ShapeJson.decodeShapes(cryofreeze)
    }

    fun export(context: Context, path: String) {
        val cryofreeze = ShapeJson.encodeShapes(_shapes.value)
        context.openFileOutput(path, Context.MODE_PRIVATE).use { it.write(cryofreeze.toByteArray()) }
    }

    fun delete() {

    }
    var worldWidth : Double = 10000.00
        set(value) {
            field = if(value < 50) {
                50.0
            } else value
        }
    var worldHeight : Double = 1000.00
        set(value) {
            field = if(value < 50) {
                50.0
            } else value
        }

    // Non-canvas updaters

    var _lineLog  = MutableStateFlow<List<Pair<String,String>>>(emptyList())

    // Animation starts here

    var loopJob : Job? = null

    fun startLoop() {
        if (loopJob?.isActive == true) return

        loopJob = viewModelScope.launch {
            while (isActive) {
                step()
                delay(16L)
            }
        }
    }

    fun stopLoop() {
        loopJob?.cancel()
        loopJob = null
    }

    private fun step() {
        val current = _shapes.value.toMutableList()

        for(shape in current) {
            shape.update(worldWidth,worldHeight)
            val line = shape.say()
            if(line != null) {
                _lineLog.value += Pair(line.name,line.line)
                microphone.speak(line.line, line.gender, line.age, line.canon)
            }

            _shapes.value = current.toList()
            _tick.value += 1
        }
    }


}