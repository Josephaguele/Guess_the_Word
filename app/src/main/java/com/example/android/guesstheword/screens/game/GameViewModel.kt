package com.example.android.guesstheword.screens.game

import android.content.IntentSender
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing all the logic needed to run the game
 */


class GameViewModel : ViewModel() {

    /*Vibration is controlled by passing in an array representing the number of milliseconds each
    interval of buzzing and non-buzzing takes. So the array [0, 200, 100, 300] will wait
    0 milliseconds, then buzz for 200ms, then wait 100ms,
    then buzz fo 300ms. Here are some example buzz patterns you can copy over:*/
    private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
    private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
    private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
    private val NO_BUZZ_PATTERN = longArrayOf(0)


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

    // Timer field
    private val timer: CountDownTimer

    // A properly encapsulated LiveData called currentTime
    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
    get() = _currentTime

    // Here we create a LiveData called currentTimeString. This will store the string version of
    // currentTime
    /*Use Transformation.map to take currentTime to a String output from currentTimeString.
What you want is to use DateUtils to convert the currentTime number output into a String.
 Then we want to emit that from the currentTimeString LiveData. To do that*/
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // This companion object has constants for our time
    companion object{
        // These represent different important times in the game, such as game length

        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }
    init {

        // This sets the value of _eventGameFinish to false. This is to signal that you've handled the game
        // finish event and that you don't need to handle it again.
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        //set the scores initial value to zero.
        _score.value = 0

        /**Copy over the CountDownTimer code and then update currentTime and eventGameFinish
         * appropriately as the timer ticks and finishes:
         */
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND)
        {
            override fun onTick(millisUntilFinished: Long)
            {
                _currentTime.value = millisUntilFinished/ ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }
        timer.start()

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
                // if word list is empty, it means the game is over. reset the list.
            resetList()
        }
            _word.value = wordList.removeAt(0)
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

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        // this is done to avoid memory leaks
        timer.cancel()
    }

}