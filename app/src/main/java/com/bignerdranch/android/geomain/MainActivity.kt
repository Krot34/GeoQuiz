package com.bignerdranch.android.geomain

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true))

    private var answeredQuestionsIndexes = listOf<Int>()

    private var currentIndex = 0
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            manageButtonActivation()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            manageButtonActivation()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            manageButtonActivation()
        }

        backButton.setOnClickListener {
            currentIndex = if (currentIndex - 1 >= 0) (currentIndex - 1) % questionBank.size else questionBank.size - 1
            updateQuestion()
            manageButtonActivation()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId: Int
        val correctAnswer = questionBank[currentIndex].answer

        answeredQuestionsIndexes += currentIndex

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            correctAnswersCount++
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (answeredQuestionsIndexes.count() == questionBank.count()) {
            displayResultMessage()
        }
    }

    private fun manageButtonActivation() {
        if (answeredQuestionsIndexes.contains(currentIndex)) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun displayResultMessage() {
        val percent = (correctAnswersCount.toFloat() / answeredQuestionsIndexes.count().toFloat() * 100.00).toInt()
        val messageResId = "Your result is $percent% correct answers"

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}