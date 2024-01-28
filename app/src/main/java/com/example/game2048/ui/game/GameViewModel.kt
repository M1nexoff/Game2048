package com.example.game2048.ui.game

import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game2048.domain.AppRepository

class GameViewModel : ViewModel(){
    private val repository: AppRepository = AppRepositoryImpl.getAppRepository()
    private val _matrixLiveData = MutableLiveData<Array<Array<Int>>>()
    val matrixLiveData: LiveData<Array<Array<Int>>> get() = _matrixLiveData
    fun moveUp() {
        repository.moveUp()
        loadData()
    }

    fun moveRight() {
        repository.moveRight()
        loadData()
    }

    fun moveDown() {
        repository.moveDown()
        loadData()
    }

    fun moveLeft() {
        repository.moveLeft()
        loadData()
    }
    fun loadData() {
        _matrixLiveData.value = repository.getMatrix()
//        _scoreLiveData.value = repository.getScore()
    }

    fun restart(){
        repository.initializeMatrix()
    }

    fun getScore(): Int{
        return repository.getScore()
    }
    fun getHighScore(): Int{
        return repository.getHighScore()
    }
    fun checkLose(): Boolean{
        return repository.checkLose()
    }

    fun loadLast(){
        repository.loadLast()
    }
    fun saveLast(){
        repository.saveLast()
    }

    fun saveNothing() {
        repository.saveNothing()
    }

    fun getLastScore() {
        repository.getLastScore()
    }
}