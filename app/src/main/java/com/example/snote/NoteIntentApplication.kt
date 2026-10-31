package com.example.snote

import android.app.Application

class NoteIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}