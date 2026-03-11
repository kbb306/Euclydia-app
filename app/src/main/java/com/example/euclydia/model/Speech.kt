package com.example.euclydia.model

import br.com.chatnoir.ggwave_kotlin.GGWaveCodec
import br.com.chatnoir.ggwave_kotlin.GGWaveSampleFormat

class Speech
 {
    private val codecs = mapOf(
        Gender.FEMALE to mapOf(
            Age.CHILD to makeCodec(2),
            Age.ADULT to makeCodec(1)
        ),
        Gender.MALE to mapOf(
            Age.CHILD to makeCodec(8),
            Age.ADULT to makeCodec(7)
        ),
        Gender.ANDROGYNOUS to mapOf(
            Age.CHILD to makeCodec(10),
            Age.ADULT to makeCodec(9)
        )
    )

        private fun makeCodec(protocol : Int) : GGWaveCodec {
        val voice = GGWaveCodec.Builder()
            .sampleRate(48000f)
            .sampleFormatInp(GGWaveSampleFormat.I16)
            .sampleFormatOut(GGWaveSampleFormat.I16)
            .protocolId(protocol)
            .volume(5)
            .build()
        return voice
    }
    fun codecFor(age: Age, gender: Gender, specialVoice: SpecialVoice? = null): GGWaveCodec? {
        return when(specialVoice) {
            SpecialVoice.SC -> makeCodec(0)
            SpecialVoice.EU -> makeCodec(6)
            null -> codecs[gender]?.get(age) ?: (makeCodec(9))
        }


    }
    suspend fun speak(text: String,gender: Gender ,age : Age) {
        val codec : GGWaveCodec? = codecFor(age,gender)
        codec?.encodeAndPlay(text)
    }
}