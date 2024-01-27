package com.example.game2048.data.source

import android.content.Context

class Pref {
    companion object {
        private lateinit var instance: Pref

        fun init(context: Context): Pref {
            if (!(::instance.isInitialized)) {
                if (!(::instance.isInitialized)) instance = Pref()
                return instance
            }else{
                return instance
            }
        }

        fun getInstance(): Pref = instance
    }
}