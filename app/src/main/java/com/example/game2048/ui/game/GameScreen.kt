package com.example.game2048.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.game2048.R
import com.example.game2048.data.model.SideEnum
import com.example.game2048.databinding.ScreenGameBinding
import com.example.game2048.utils.MyTouchListener
import com.example.game2048.utils.getNumberIcon

class GameScreen(val isNew:Boolean) : Fragment(R.layout.screen_game) {
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val viewModel: GameViewModel by viewModels()
    private val views = mutableListOf<AppCompatTextView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref: SharedPreferences =
            requireContext().getSharedPreferences("AAA", Context.MODE_PRIVATE)

        if (!isNew){
            viewModel.loadLast()
        }else{
            viewModel.getLastScore()
            viewModel.restart()
        }
        if (pref.getBoolean("aa", true)){
            viewModel.restart()
        }
        pref.edit().putBoolean("aa",false).apply()
        loadViews()
        binding.lose.visibility = View.GONE
        setupObservers()
//        viewModel.scoreLiveData.observe(viewLifecycleOwner) { score ->
//            updateScore(score)
//        }
        attachTouchListener()
        binding.restart.setOnClickListener {
            binding.lose.visibility = View.GONE
            binding.outerContainer.isClickable = true
            viewModel.restart()
            viewModel.loadData()
        }
        viewModel.loadData()
    }

    private fun loadViews() {
        for (i in 0 until binding.outerContainer.childCount) {
            val line = binding.outerContainer[i] as LinearLayoutCompat
            for (j in 0 until line.childCount) {
                views.add(line[j] as AppCompatTextView)
            }
        }
    }

    private fun setupObservers() {
        viewModel.matrixLiveData.observe(viewLifecycleOwner) { matrix ->
            updateUI(matrix)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener() {
        val myTouchListener = MyTouchListener(requireContext())
        myTouchListener.setActionSideEnumListener {
            when (it) {
                SideEnum.DOWN -> viewModel.moveDown()
                SideEnum.RIGHT -> viewModel.moveRight()
                SideEnum.UP -> viewModel.moveUp()
                SideEnum.LEFT -> viewModel.moveLeft()
            }
            setupObservers()
        }
        binding.outerContainer.setOnTouchListener(myTouchListener)
    }

    private fun updateUI(matrix: Array<Array<Int>>) {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                val textView = views[i * 4 + j]
                if (matrix[i][j] == 0) {
                    textView.text = ""
                } else {
                    textView.text = "${matrix[i][j]}"
                }

                textView.setBackgroundResource(requireContext().getNumberIcon(matrix[i][j]))

//                animateTileMovement(textView, i, j)
            }
        }
        if (viewModel.checkLose()){
            binding.outerContainer.isClickable = false
            binding.lose.visibility = View.VISIBLE
            viewModel.saveNothing()
        }
        binding.score.text = "${viewModel.getScore()}"
        binding.highScore.text = "${viewModel.getHighScore()}"
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveLast()
    }
}