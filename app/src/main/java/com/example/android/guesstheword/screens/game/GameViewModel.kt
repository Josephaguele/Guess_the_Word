package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel : ViewModel() {

    // The current word
    private var _word = MutableLiveData<String>() // This is done for data privacy
    val word : LiveData<String> // This serves as a getter method for the MutableLiveData "word"
        get() = _word

    // The current score is changed to a LiveData
    private var _score = MutableLiveData<Int>() // LiveData is mutable internally
    val score : LiveData<Int> // due to the type specified, the code is
    // going to be exposed externally as LiveData so it can't be change
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // A properly encapsulated LiveData called eventGameFinish
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish

    init {
        // This sets the fvalue of _eventGameFinish to false. This is to signal that you've handled the game
        // finish event and that you don't need to handle it again.
        _eventGameFinish.value = false
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        //set the scores initial value to zero.
        _score.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //gameFinished()
                // if word list is empty, it means the game is over. eventGameFinish is true.
            _eventGameFinish.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }

    }

    /** Methods for buttons presses **/
    fun onSkip() {
        // adding references to score, and adding null safety checks
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /** Methods for completed events**/
    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

}