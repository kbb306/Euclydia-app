package com.example.euclydia.model
import br.com.chatnoir.ggwave_kotlin.*
class Speech(
    private val codec : GGWaveCodec
) {
    suspend fun speak(text: String) {
        codec.encodeAndPlay(text)
    }
}