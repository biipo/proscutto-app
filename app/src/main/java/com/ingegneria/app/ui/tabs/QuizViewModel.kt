package com.ingegneria.app.ui.tabs

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.ui.screens.PetViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class QuizViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("quiz")
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _isQuestionsLoaded = MutableStateFlow(false)
    val isQuestionsLoaded: StateFlow<Boolean> = _isQuestionsLoaded

    private val _answeredQuestionsCount = MutableStateFlow(0)
    val answeredQuestionsCount: StateFlow<Int> = _answeredQuestionsCount

    val currentQuestionIndex = mutableIntStateOf(0)
    val correctCount = mutableIntStateOf(0)

    init {
        currentUser?.let { user ->
            resetDailyQuestionLimitIfNeeded(user.uid)
            loadQuestions()
        }
    }

    private fun loadQuestions() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedQuestions = snapshot.children.mapNotNull { quizSnapshot ->
                    quizSnapshot.getValue(Question::class.java)
                }
                _questions.value = loadedQuestions.shuffled()
                _isQuestionsLoaded.value = true
            }

            override fun onCancelled(error: DatabaseError) {
                _isQuestionsLoaded.value = false
            }
        })
    }

    private fun resetDailyQuestionLimitIfNeeded(userId: String) {
        val currentDate = Calendar.getInstance().time

        firestore.collection("users").document(userId).get().addOnSuccessListener { document ->
            val lastLoginTimestamp = document.getTimestamp("lastLogin")?.toDate()
            val isNewDay = lastLoginTimestamp == null || isNextDay(lastLoginTimestamp, currentDate)

            if (isNewDay) {
                firestore.collection("users").document(userId).update(
                    mapOf(
                        "dailyQuestionLimit" to 0,
                        "lastLogin" to FieldValue.serverTimestamp()
                    )
                ).addOnSuccessListener {
                    _answeredQuestionsCount.value = 0
                }
            } else {
                _answeredQuestionsCount.value = document.getLong("dailyQuestionLimit")?.toInt() ?: 0
            }
        }
    }

    fun updateAnsweredQuestionsCount() {
        currentUser?.let { user ->
            val newCount = _answeredQuestionsCount.value + 1
            firestore.collection("users").document(user.uid).update("dailyQuestionLimit", newCount)
                .addOnSuccessListener {
                    _answeredQuestionsCount.value = newCount
                }
        }
    }

    fun finalResult(correctCount: Int, petVM: PetViewModel) {
        when (correctCount) {
            in 2..3 -> { // more than 3 correct answers but less than 5
                viewModelScope.launch {
                    petVM.isPetFbInit.collect { isInitialized ->
                        if (isInitialized) {
                            petVM.petFb!!.increaseMult(0.5)
                        }
                    }
                }
            }
            4 -> { // 5 correct answers
                viewModelScope.launch {
                    petVM.isPetFbInit.collect { isInitialized ->
                        if (isInitialized) {
                            petVM.petFb!!.increaseMult(1.0)
                        }
                    }
                }
            }
            else -> {
                viewModelScope.launch {
                    petVM.isPetFbInit.collect { isInitialized ->
                        if (isInitialized) {
                            petVM.petFb!!.resetMult()
                        }
                    }
                }
            }
        }
        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId)
                .update("quizCompleted", FieldValue.increment(1))
                .addOnSuccessListener {
                    Log.d("QuizViewModel", "Quiz completed incremented")
                }
        }
    }

    private fun isNextDay(lastDate: Date, currentDate: Date): Boolean {
        val lastCalendar = Calendar.getInstance().apply { time = lastDate }
        val currentCalendar = Calendar.getInstance().apply { time = currentDate }
        return lastCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR) ||
                lastCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)
    }
}
