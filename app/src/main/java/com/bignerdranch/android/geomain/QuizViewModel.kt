package com.bignerdranch.android.geomain

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var correctAnswersCount = 0
    var answeredQuestionsIndexes = intArrayOf()
    var cheatedQuestionsIndexes = intArrayOf()

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    val currentQuestionIsCheated: Boolean
        get() = cheatedQuestionsIndexes.contains(currentIndex)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionIsAnswered: Boolean
        get() = answeredQuestionsIndexes.contains(currentIndex)

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val questionCount: Int
        get() = questionBank.count()

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex - 1 >= 0) (currentIndex - 1) % questionBank.size else questionBank.size - 1
    }
}