package com.example.euclydia.model

import android.content.Context
import br.com.chatnoir.ggwave_kotlin.GGWaveCodec
import br.com.chatnoir.ggwave_kotlin.GGWaveSampleFormat
import com.example.euclydia.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Speech (
    private val scope : CoroutineScope,
    private val context : Context
)
 {
     companion object {
         private val codecs = mapOf(
             Gender.FEMALE to mapOf(
                 Age.CHILD to 2,
                 Age.ADULT to 1,
             ),
             Gender.MALE to mapOf(
                 Age.CHILD to 8,
                 Age.ADULT to 7,
             ),
             Gender.ANDROGYNOUS to mapOf(
                 Age.CHILD to 10,
                 Age.ADULT to 9
             )
         )

         val reverseLookup = codecs.flatMap { (gender, ages) ->
             ages.map { (age, codec) ->
                 codec to (gender to age)
             }
         }.toMap()

         private val oldIDs = mapOf(
             "SC" to 0,
             "FA" to 1,
             "FC" to 2,
             "EU" to 6,
             "MA" to 7,
             "MC" to 8
         )

         fun reverseBS(code: String): VoiceRecord {
             return when (val protocol: Int? = oldIDs[code]) {
                 0 -> VoiceRecord(Age.ADULT, Gender.FEMALE, SpecialVoice.SC)
                 6 -> VoiceRecord(Age.ADULT, Gender.MALE, SpecialVoice.EU)
                 else -> VoiceRecord(
                     (reverseLookup[protocol]!!).second,
                     (reverseLookup[protocol]!!).first
                 )
             }
         }

         fun arrayFor(age: Age, canon: SpecialVoice?): Int {
             return when (canon) {
                 SpecialVoice.SC -> R.array.scalene_lines
                 SpecialVoice.EU -> R.array.euclid_lines
                 SpecialVoice.BILL -> R.array.bill_lines
                 null -> when (age) {
                     Age.ADULT -> R.array.adult_lines
                     Age.CHILD -> R.array.child_lines
                 }
             }
         }

         private fun makeCodec(protocol: Int) : GGWaveCodec {
             val voice = GGWaveCodec.Builder()
                 .sampleRate(48000f)
                 .sampleFormatInp(GGWaveSampleFormat.I16)
                 .sampleFormatOut(GGWaveSampleFormat.I16)
                 .protocolId(protocol)
                 .volume(5)
                 .build()
             return voice
         }

         private val codecCache = mapOf(
             0 to makeCodec(0),
             1 to makeCodec(1),
             2 to makeCodec(2),
             6 to makeCodec(6),
             7 to makeCodec(7),
             8 to makeCodec(8),
             9 to makeCodec(9),
             10 to makeCodec(10)
         )
     }

     data class VoiceRecord(
         val age: Age,
         val gender: Gender,
         val canon: SpecialVoice? = null
     )



    fun codecFor(age: Age, gender: Gender, specialVoice: SpecialVoice? = null): GGWaveCodec? {
        val protocol = when(specialVoice) {
            SpecialVoice.SC -> 0
            SpecialVoice.EU -> 6
            SpecialVoice.BILL -> 8
            null -> codecs[gender]?.get(age) ?: 9
        }
        return codecCache[protocol]
    }


    fun speak(request: Shape.SpeechRequest): String {
        val line = context.resources.getStringArray(arrayFor(request.age,request.canon)).random()
        scope.launch {
            val codec : GGWaveCodec ?= codecFor(request.age,request.gender,request.canon)
            delay((1000..6000).random().toLong())
            codec?.encodeAndPlay(line)
    }
        return line
    }
}