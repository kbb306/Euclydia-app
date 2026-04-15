package com.example.euclydia.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SoundOption {
    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    fun setMuted(muted: Boolean) {
        _isMuted.value = muted
    }

    fun toggle() {
        _isMuted.value = !_isMuted.value
    }

    fun on() {
        _isMuted.value = true
    }

    fun off() {
        _isMuted.value = false
    }
}