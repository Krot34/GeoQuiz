package com.bignerdranch.android.geomain

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : ComponentActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

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
            quizViewModel.moveToNext()
            updateQuestion()
            manageButtonActivation()
        }

        backButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
            manageButtonActivation()
        }

        manageButtonActivation()
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putIntArray(KEY_INDEX, quizViewModel.answeredQuestionsIndexes)
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId: Int
        val correctAnswer = quizViewModel.currentQuestionAnswer

        quizViewModel.answeredQuestionsIndexes += quizViewModel.currentIndex

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.correctAnswersCount++
        } else {
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (quizViewModel.answeredQuestionsIndexes.count() == quizViewModel.questionCount) {
            displayResultMessage()
        }
    }

    private fun manageButtonActivation() {
        if (quizViewModel.answeredQuestionsIndexes.contains(quizViewModel.currentIndex)) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun displayResultMessage() {
        val percent = (quizViewModel.correctAnswersCount.toFloat() / quizViewModel.answeredQuestionsIndexes.count().toFloat() * 100.00).toInt()
        val messageResId = "Your result is $percent% correct answers"

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}