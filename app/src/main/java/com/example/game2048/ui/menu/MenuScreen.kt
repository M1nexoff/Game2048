package com.example.game2048.ui.menu

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.game2048.R
import com.example.game2048.databinding.ScreenMenuBinding
import com.example.game2048.ui.game.GameScreen
import com.example.game2048.utils.replaceScreen

class MenuScreen : Fragment(R.layout.screen_menu) {
    private val binding by viewBinding(ScreenMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.play.setOnClickListener {
            replaceScreen(GameScreen(false))
        }

        binding.newGame.setOnClickListener {
            replaceScreen(GameScreen(true))
        }

        binding.exit.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Exit Game")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton("No", null)
            .create()

        alertDialog.show()
    }
}
