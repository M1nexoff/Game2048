package com.example.game2048.data.source

import android.content.Context
import android.content.SharedPreferences

class Pref private constructor(context: Context) {
    companion object {
        private lateinit var instance: Pref

        fun init(context: Context): Pref {
            if (!(::instance.isInitialized)) {
                instance = Pref(context)
            }
            return instance
        }

        fun getInstance(): Pref = instance
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Game2048Prefs", Context.MODE_PRIVATE)

    private val HIGH_SCORE_KEY = "HIGH_SCORE"
    private val MATRIX_KEY = "MATRIX"

    fun getHighScore(): Int {
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }

    fun setHighScore(score: Int) {
        sharedPreferences.edit().putInt(HIGH_SCORE_KEY, score).apply()
    }
    fun getLastScore(): Int {
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }

    fun setLastScore(score: Int) {
        sharedPreferences.edit().putInt(HIGH_SCORE_KEY, score).apply()
    }

    fun saveLast(matrix: Array<Array<Int>>) {
        val matrixString = matrix.flatten().joinToString(",")
        sharedPreferences.edit().putString(MATRIX_KEY, matrixString).apply()
    }

    fun loadLast(): Array<Array<Int>> {
        val matrixString = sharedPreferences.getString(MATRIX_KEY, "")
        return if (matrixString.isNullOrEmpty()) {
            Array(4) { Array(4) { 0 } }
        } else {
            val matrixValues = matrixString.split(",").map { it.toInt() }
            val matrixSize = 4
            Array(matrixSize) { i ->
                Array(matrixSize) { j ->
                    matrixValues[i * matrixSize + j]
                }
            }
        }
    }
}
