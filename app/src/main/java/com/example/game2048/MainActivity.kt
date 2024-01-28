package com.example.game2048

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.game2048.ui.game.GameScreen
import com.example.game2048.ui.menu.MenuScreen
import com.example.game2048.utils.addScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            addScreen(MenuScreen())
        }, 500)
    }
}