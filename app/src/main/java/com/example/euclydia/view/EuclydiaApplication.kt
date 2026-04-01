package com.example.euclydia.view

import android.app.Application
import com.example.euclydia.database.Repository

class EuclydiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}