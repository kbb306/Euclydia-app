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
import com.example.euclydia.model.ShapeStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update


data class LineLogEntry(
    val uuid: UUID,
    val name: String,
    val line: String,
    val tick: Long
)

class EuclydiaViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = Repository.get()
    val shapeList = ShapeStore.shapes
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
    val followedShape: Shape?
        get() = shapeList.value.firstOrNull { it.uuid == _followedUUID.value }
    val followedX : Double?
        get() = followedShape?.x

    val followedY : Double?
        get() = followedShape?.y

    val followedName : String?
        get() = followedShape?.name



    val select_ids = mutableSetOf<UUID>()


    var zygote : DNA = DNA( // Create fragment will modify this and send it to create()
        UUID.randomUUID(),
        "Bill",
        Age.CHILD,
        Gender.MALE,
        Color.YELLOW,
        3,
        3.33,
        500.0,
        300.0,
        90.00,
        5.00,
        SpecialVoice.BILL
    )



    // Import/Export and dependencies


    fun create(dna: DNA) {
        dna.uuid = UUID.randomUUID()// used for import() and standard shape creation
        val newShape = Shape(dna)
        ShapeStore.addShape(newShape)
        syncInator()
    }


    fun legacyImport(context : Context, path: String) { //For loading Euclydia3.0 save files if I can figure out how to open them
        val loaded = mutableListOf<Shape>()
        csvReader().open(context.openFileInput(path)) {
            readAllAsSequence().forEach { row ->
                loaded.add(Shape(row as List<Any>))
            }
        }
        ShapeStore.addShapes(loaded)
    }

    fun import(context : Context,path: String) {
        val cryofreeze = context.openFileInput(path)
            .bufferedReader().use { it.readText() }
        ShapeStore.addShapes(ShapeJson.decodeShapes(cryofreeze))
    }

    fun export(context: Context, path: String) {
        val cryofreeze = ShapeJson.encodeShapes(shapeList.value)
        context.openFileOutput(path, Context.MODE_PRIVATE).use { it.write(cryofreeze.toByteArray()) }
    }

    fun delete(UUIDs : List<UUID>) {
        ShapeStore.removeShapes(UUIDs)
        syncInator()
    }

    fun follow(shape: Shape) {
        follow(shape.uuid)
    }

    fun collisionCheck(shape: Shape, current : List<Shape>) {
        val mightCollide =
            current.filter { it.uuid != shape.uuid && it.distance(shape) < shape.radius + it.radius }
        if (!mightCollide.isEmpty()) {
            var dx = 0.00
            var dy = 0.00

            for (other in mightCollide) {
                dx += shape.x - other.x
                dy += shape.y - other.y
            }
            if (dx != 0.00 || dy != 0.00) {
                shape.back(5.0)
                shape.turnTo(Math.toDegrees(kotlin.math.atan2(dy,dx)))
                shape.forward(5.0)
            }
        }
    }

    fun follow(uuid : UUID) {
        _followedUUID.value = uuid
        shapeList.value.forEach { shape ->
            shape.isFollowed = (shape.uuid == uuid)
        }
    }

    fun unfollow() {
        shapeList.value.forEach { shape ->
            if (shape.uuid == _followedUUID.value) {
                shape.isFollowed = false
            }
        }
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
            repo.insertShapes(shapeList.value)
        }
    }

    // Non-canvas updaters

    private val _lineLog  = MutableStateFlow<List<LineLogEntry>>(emptyList())
    val lineLog: StateFlow<List<LineLogEntry>> = _lineLog.asStateFlow()
    @OptIn(InternalSerializationApi::class)


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
        if (loopJob != null) return
        loopJob = viewModelScope.launch {
            while (isActive) {
                step()
                delay(33L)
            }
        }
    }

    fun stopLoop() {
        loopJob?.cancel()
        loopJob = null
    }

    private fun step() {
        ShapeStore.mutate { current ->
            for (shape in current) {
                shape.update(worldHeight, worldWidth)
                if (_tick.value >= shape.nextSpeechTick) {
                    val request = shape.say()
                    if (request != null) {
                        _lineLog.update { old ->
                            (old + LineLogEntry(
                                uuid = shape.uuid,
                                request.speakerName,
                                microphone.speak(request),
                                _tick.value
                            )
                                    ).takeLast(200)
                        }
                    }
                    shape.nextSpeechTick = _tick.value + 180L
                }
                collisionCheck(shape, current)
            }
        }
        _tick.value += 1
    }

    fun load() {
        viewModelScope.launch {
            val first = repo.shapeList.first()
            ShapeStore.setShapes(first)
        }
    }

}