package com.example.euclydia.model

import br.com.chatnoir.ggwave_kotlin.GGWaveCodec
import br.com.chatnoir.ggwave_kotlin.GGWaveSampleFormat

class Speech
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
             ages.map {(age,codec) ->
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

         fun reverseBS(code : String): VoiceRecord  {
             return when(val protocol : Int? = oldIDs[code]) {
                 0 -> VoiceRecord(Age.ADULT, Gender.FEMALE, SpecialVoice.SC)
                 6 -> VoiceRecord(Age.ADULT, Gender.FEMALE, SpecialVoice.EU)
                 else -> VoiceRecord((reverseLookup[protocol]!!).second,(reverseLookup[protocol]!!).first)
             }
         }

     }

     data class VoiceRecord(
         val age: Age,
         val gender: Gender,
         val canon: SpecialVoice? = null
     )


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

    fun codecFor(age: Age, gender: Gender, specialVoice: SpecialVoice? = null): GGWaveCodec {
        return when(specialVoice) {
            SpecialVoice.SC -> makeCodec(0)
            SpecialVoice.EU -> makeCodec(6)
            null -> makeCodec(codecs[gender]?.get(age) ?: 9)
        }
    }

    suspend fun speak(text: String,gender: Gender,age : Age, canon : SpecialVoice?) {
        val codec : GGWaveCodec = codecFor(age,gender,canon)
        codec.encodeAndPlay(text)
    }
}