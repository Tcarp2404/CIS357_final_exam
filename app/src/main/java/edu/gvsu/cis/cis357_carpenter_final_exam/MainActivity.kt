package edu.gvsu.cis.cis357_carpenter_final_exam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import edu.gvsu.cis.cis357_carpenter_final_exam.data.model.DisneyCharacter
import edu.gvsu.cis.cis357_carpenter_final_exam.ui.screens.DetailScreen
import edu.gvsu.cis.cis357_carpenter_final_exam.ui.screens.MainScreen
import edu.gvsu.cis.cis357_carpenter_final_exam.ui.viewmodel.DisneyViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DisneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedCharacter by remember { mutableStateOf<DisneyCharacter?>(null) }

            if (selectedCharacter == null) {
                MainScreen(
                    viewModel = viewModel,
                    onCharacterClick = { selectedCharacter = it }
                )
            } else {
                DetailScreen(
                    character = selectedCharacter!!,
                    viewModel = viewModel,
                    onBack = { selectedCharacter = null }
                )
            }
        }
    }
}