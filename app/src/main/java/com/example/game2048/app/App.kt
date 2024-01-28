package com.example.game2048.app

import AppRepositoryImpl
import android.app.Application
import com.example.game2048.data.source.Pref

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Pref.init(this)
        AppRepositoryImpl.init()
    }
}