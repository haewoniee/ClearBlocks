package com.example.clearcell.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    private val score = MutableLiveData<Int>()

    fun getScore(): LiveData<Int> {
        return score
    }

    fun setScore(newScore: Int) {
        score.value = newScore
    }

}