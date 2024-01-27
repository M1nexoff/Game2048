package com.example.game2048.ui.game

import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.game2048.R
import com.example.game2048.databinding.ScreenGameBinding
class GameScreen : Fragment(R.layout.screen_game){
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val viewModel: GameViewModel by viewModels()
    private val views = mutableListOf<AppCompatTextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadViews()
    }
    private fun loadViews() {
        for (i in 0 until binding.outerContainer.childCount) {
            val line = binding.outerContainer[i] as LinearLayoutCompat
            for (j in 0 until line.childCount) {
                views.add(line[j] as AppCompatTextView)
            }
        }
    }
}