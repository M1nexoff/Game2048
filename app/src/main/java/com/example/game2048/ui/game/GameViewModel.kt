package com.example.game2048.ui.game

import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){
    private val views = MutableLiveData<ArrayList<AppCompatTextView>>(ArrayList(16))

}