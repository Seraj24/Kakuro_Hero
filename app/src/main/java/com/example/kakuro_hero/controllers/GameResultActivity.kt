package com.example.kakuro_hero.controllers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kakuro_hero.models.GameResult
import com.example.kakuro_hero.services.GameSessionHolder
import com.example.kakuro_hero.views.GameResultView
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme

class GameResultActivity : ComponentActivity() {

    val gameSession = GameSessionHolder.currentSession ?: error("GameSession is missing.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameResultView().GameResultUI(
                        onPlayAgainClick = { navigateToGame() },
                        onHomeClick = { navigateToHome() },
                        gameSession = gameSession,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun navigateToGame() {
        GameSessionHolder.currentSession = null
        startActivity(Intent(this, GameSetupActivity::class.java))
        finish()
    }

    private fun navigateToHome() {
        GameSessionHolder.currentSession = null
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview6() {
        GameResultView().GameResultUI(
            onPlayAgainClick = { navigateToGame() },
            onHomeClick = { navigateToHome() },
            gameSession = gameSession,
            modifier = Modifier
        )
    }
}



