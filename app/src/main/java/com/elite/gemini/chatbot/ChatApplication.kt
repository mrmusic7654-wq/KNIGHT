package com.elite.gemini.chatbot

import android.app.Application

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ChatApplication
            private set
    }
}
