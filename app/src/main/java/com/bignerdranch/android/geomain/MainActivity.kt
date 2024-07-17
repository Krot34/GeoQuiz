package com.bignerdranch.android.geomain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX_CURRENT = "current"
private const val KEY_INDEX_ANSWERED = "answered"
private const val KEY_INDEX_CHEATED = "cheated"
private const val REQUEST_NAME_CHEAT = "CheatActivity"

class MainActivity : ComponentActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private var isCheater = false
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX_CURRENT, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        isCheater = intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        if (isCheater) {
            quizViewModel.cheatedQuestionsIndexes += currentIndex
        }

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        backButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

//        val answeredQuestionsIndexes = savedInstanceState?.getIntArray(KEY_INDEX_ANSWERED)
//        if (answeredQuestionsIndexes != null) {
//            quizViewModel.answeredQuestionsIndexes = answeredQuestionsIndexes
//        }
//
//        val cheatedQuestionsIndexes = savedInstanceState?.getIntArray(KEY_INDEX_CHEATED)
//        if (cheatedQuestionsIndexes != null) {
//            quizViewModel.cheatedQuestionsIndexes = cheatedQuestionsIndexes
//        }

        updateQuestion()
        manageButtonActivation()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getBooleanExtra(REQUEST_NAME_CHEAT, false) ?: false
            }
        }

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

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            launcher?.launch(CheatActivity.newIntent(this, answerIsTrue))
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX_CURRENT, quizViewModel.currentIndex)
        outState.putIntArray(KEY_INDEX_ANSWERED, quizViewModel.answeredQuestionsIndexes)
        outState.putIntArray(KEY_INDEX_CHEATED, quizViewModel.cheatedQuestionsIndexes)
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

        if (quizViewModel.currentQuestionIsCheated && userAnswer == correctAnswer) {
            messageResId = R.string.judgment_toast
            quizViewModel.correctAnswersCount++
        }
        else if (userAnswer == correctAnswer) {
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
        if (quizViewModel.currentQuestionIsAnswered) {
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

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_SHOWN, true)
            }
        }
    }
}