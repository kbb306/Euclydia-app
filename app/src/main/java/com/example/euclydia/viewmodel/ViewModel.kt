package com.example.euclydia.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.euclydia.model.Age
import com.example.euclydia.database.DNA
import com.example.euclydia.database.Repository
import com.example.euclydia.model.Gender
import com.example.euclydia.model.Shape
import com.example.euclydia.model.ShapeJson
import com.example.euclydia.model.SpecialVoice
import com.example.euclydia.model.Speech
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.InternalSerializationApi
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.flow.combine


data class LineLogEntry(
    val uuid: UUID,
    val name: String,
    val line: String,
    val tick: Long
)

class EuclydiaViewModel(application: Application) : AndroidViewModel(application) {
    private val _shapes = MutableStateFlow<List<Shape>>(emptyList())
    private val repo = Repository.get()
    val shapeList : StateFlow<List<Shape>> = _shapes.asStateFlow()
    val savedShapes: StateFlow<List<Shape>> =
        repo.shapeList.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _tick = MutableStateFlow(0L)
    val tick : StateFlow<Long> = _tick.asStateFlow()

    private var microphone = Speech(viewModelScope,application)
    private val _followedUUID = MutableStateFlow<UUID?>(null)
    val followedUUID: StateFlow<UUID?> = _followedUUID.asStateFlow()
    val followedShape : Shape?
        get() = followedUUID?.let { uuid ->
            _shapes.value.firstOrNull {it.uuid == uuid}
        }
    val followedX : Double?
        get() = followedShape?.x

    val followedY : Double?
        get() = followedShape?.y

    val followedName : String?
        get() = followedShape?.name



    val select_ids = mutableSetOf<UUID>()


    @OptIn(InternalSerializationApi::class)
    var zygote : DNA = DNA( // Create fragment will modify this and send it to create()
        UUID.randomUUID(),
        "Bill",
        Age.CHILD,
        Gender.MALE,
        Color.YELLOW,
        3,
        3.33,
        Random.nextDouble(),
        Random.nextDouble(),
        90.00,
        5.00,
        SpecialVoice.BILL
    )



    // Import/Export and dependencies
    fun create(name : String, age: Age, gender: Gender, color : Int, sides : Int, // Raw data create(), may be deprecated soon?
              length : Double, x : Double, y : Double, heading : Double, speed :
               Double, ) {
        val newShape  = Shape(name,age,gender,color,sides,length,x,y,heading,speed)
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

    fun delete(UUIDs : List<UUID>) {
        _shapes.value = _shapes.value.filter { it.uuid !in UUIDs }
    }

    fun follow(shape: Shape) {
        follow(shape.uuid)
    }

    fun collisionCheck(shape: Shape) {
        val mightCollide = _shapes.value.filter { it.uuid != shape.uuid && it.distance(shape) < shape.radius + it.radius}
        if (!mightCollide.isEmpty()) {
            shape.back(.45)
            var safe = false
            var newHeading = 0.00
            while (!safe) {
                for (each in mightCollide) {
                    newHeading = shape.avoid(each)
                    safe = (mightCollide.none { it.heading != newHeading })
                }
            }
            shape.turnTo(newHeading)
            shape.forward(5.00)
    }}

    fun follow(uuid : UUID) {
        _followedUUID.value = uuid
        _shapes.value.forEach { shape ->
            shape.isFollowed = (shape.uuid == uuid)
        }
    }

    fun unfollow() {
        _followedUUID.value = null
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

    fun syncInator() {
        viewModelScope.launch {
            repo.clear()
            repo.insertShapes(_shapes.value)
        }
    }

    // Non-canvas updaters

    private val _lineLog  = MutableStateFlow<List<LineLogEntry>>(emptyList())
    val lineLog: StateFlow<List<LineLogEntry>> = _lineLog.asStateFlow()
    @OptIn(InternalSerializationApi::class)
    fun moreInfo(uuid : UUID): DNA {
        return (_shapes.value.filter { it.uuid == uuid}).first().export()
    }

    val followedLineLog: StateFlow<List<LineLogEntry>> =
        combine(_lineLog, _followedUUID) { log, uuid ->
            if (uuid == null) {
                emptyList()
            } else {
                log.filter { it.uuid == uuid }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
            shape.update(worldHeight,worldWidth)
            val request = shape.say()
            if (request != null) {
                _lineLog.value += LineLogEntry(
                    uuid = shape.uuid,
                    request.speakerName,
                    microphone.speak(request),
                    _tick.value
                )
            }
            collisionCheck(shape)
        }
        _shapes.value = current.toList()
        _tick.value += 1
    }


}