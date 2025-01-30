package com.ingegneria.app.ui.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.PropertyName
import com.ingegneria.app.ui.screens.PetViewModel


data class Question(
    @get:PropertyName("question") @set:PropertyName("question")
    var question: String = "",

    @get:PropertyName("options") @set:PropertyName("options")
    var options: List<String> = emptyList(),

    @get:PropertyName("correct_answer") @set:PropertyName("correct_answer")
    var correctAnswer: String = ""
)

@Composable
fun Quiz(navController: NavController, quizViewModel: QuizViewModel, petVM: PetViewModel) {
    val context = LocalContext.current
    val questions by quizViewModel.questions.collectAsState()
    val isQuestionsLoaded by quizViewModel.isQuestionsLoaded.collectAsState()
    val answeredQuestionsCount by quizViewModel.answeredQuestionsCount.collectAsState()
    val dailyQuestionLimit = 5

    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    Log.e("QUESTION PAGE", "BEGINNING OF THE PAGE")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            answeredQuestionsCount >= dailyQuestionLimit -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Come back tomorrow!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                quizViewModel.finalResult(quizViewModel.correctCount.intValue, petVM)
            }

            isQuestionsLoaded && questions.isNotEmpty() -> {
                val currentQuestion = questions[quizViewModel.currentQuestionIndex.intValue]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    currentQuestion.options.forEach { option ->
                        AnswerButton(
                            answer = option,
                            isSelected = option == selectedAnswer,
                            onClick = { selectedAnswer = option }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (selectedAnswer == currentQuestion.correctAnswer) {
                                petVM.petFb?.gainXp(2)
                                quizViewModel.correctCount.intValue += 1
                                feedbackMessage = "Correct Answer!"
                            } else {
                                feedbackMessage = "Wrong answer! The correct answer was: ${currentQuestion.correctAnswer}"
                            }

                            quizViewModel.updateAnsweredQuestionsCount()
                            selectedAnswer = null
                            quizViewModel.currentQuestionIndex.intValue =
                                (quizViewModel.currentQuestionIndex.intValue + 1) % questions.size
                        },
                        enabled = selectedAnswer != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Submit")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    feedbackMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (it.startsWith("Correct Answer")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }

            !isQuestionsLoaded -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AnswerButton(answer: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text = answer)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuiz(navController: NavController = rememberNavController()) {
//    Quiz(navController = navController)
}
