package com.example.euclydia.viewmodel

import br.com.chatnoir.ggwave_kotlin.GGWaveCodec

class Speech(
    private val codec : GGWaveCodec
) {
    suspend fun speak(text: String) {
        codec.encodeAndPlay(text)
    }
}