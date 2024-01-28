package com.example.game2048.domain

interface AppRepository {
    fun getMatrix(): Array<Array<Int>>
    fun initializeMatrix()
    fun getScore(): Int
    fun getHighScore(): Int
    fun moveUp()
    fun moveRight()
    fun moveDown()
    fun moveLeft()
    fun checkLose(): Boolean
    fun loadLast()
    fun saveLast()
    fun saveNothing()
    fun getLastScore(): Int
}