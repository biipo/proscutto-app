package com.ingegneria.app.ui.tabs

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*
import com.google.firebase.database.PropertyName
import android.content.Context
import androidx.compose.ui.platform.LocalContext





data class Question(
    @get:PropertyName("question") @set:PropertyName("question")
    var question: String = "",

    @get:PropertyName("options") @set:PropertyName("options")
    var options: List<String> = emptyList(),

    @get:PropertyName("correct_answer") @set:PropertyName("correct_answer")
    var correctAnswer: String = ""
)

@Composable
fun Quiz(navController: NavController) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().reference.child("quiz")
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isQuestionsLoaded by remember { mutableStateOf(false) }
    var answeredQuestionsCount by remember {
        mutableIntStateOf(loadAnsweredQuestionsCount(context))
    }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val dailyQuestionLimit = 5

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedQuestions = snapshot.children.mapNotNull { quizSnapshot ->
                    quizSnapshot.getValue(Question::class.java)
                }

                questions = loadedQuestions.shuffled()
                isQuestionsLoaded = true
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Quiz", "Errore: ${error.message}")
            }
        })
    }

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
                        text = "Le domande giornaliere sono terminate. A domani!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            isQuestionsLoaded && questions.isNotEmpty() -> {
                val currentQuestion = questions[currentQuestionIndex]

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
                            print(currentQuestion.correctAnswer)

                            feedbackMessage = if (selectedAnswer == currentQuestion.correctAnswer) {
                                "Risposta corretta!"
                            } else {
                                "Risposta sbagliata! La risposta corretta era: ${currentQuestion.correctAnswer}"
                            }

                            answeredQuestionsCount++
                            saveAnsweredQuestionsCount(context, answeredQuestionsCount)

                            selectedAnswer = null
                            currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
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
                            color = if (it.startsWith("Risposta corretta")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
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
                        text = "Errore: Nessuna domanda disponibile.",
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
    Quiz(navController = navController)
}

fun saveAnsweredQuestionsCount(context: Context, count: Int) {
    val sharedPreferences = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("answered_questions_count", count)
        apply()
    }
}

fun loadAnsweredQuestionsCount(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("answered_questions_count", 0)
}








/*
class Quiz : Fragment() {

    private var _binding: FragmentQuizBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuizBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/