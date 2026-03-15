package com.example.kakuro_hero.controllers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kakuro_hero.models.GameMode
import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.GameResult
import com.example.kakuro_hero.models.GameSession
import com.example.kakuro_hero.models.GridSize
import com.example.kakuro_hero.models.PressureType
import com.example.kakuro_hero.models.GameSetup
import com.example.kakuro_hero.models.KakuroGrid
import com.example.kakuro_hero.models.Puzzle
import com.example.kakuro_hero.models.ChallengeConstraints
import com.example.kakuro_hero.models.HintConstraint
import com.example.kakuro_hero.models.HintConstraints
import com.example.kakuro_hero.models.HintType
import com.example.kakuro_hero.services.GameSessionHolder
import com.example.kakuro_hero.services.KakuroGenerator
import com.example.kakuro_hero.views.GameSetupView
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme

class GameSetupActivity : ComponentActivity() {

    private var setup by mutableStateOf(GameSetup())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameSetupView().GameSetupUI(
                        selectedGameMode = setup.gameMode,
                        selectedDifficulty = setup.difficulty,
                        selectedGridSize = setup.gridSize,
                        selectedPressureType = setup.pressureType,

                        onGameModeSelected = { selectedMode ->
                            setup = if (selectedMode == GameMode.RELAXING) {
                                setup.copy(
                                    gameMode = selectedMode,
                                    pressureType = null
                                )
                            } else {
                                setup.copy(
                                    gameMode = selectedMode,
                                    pressureType = setup.pressureType ?: PressureType.TIME_BASED
                                )
                            }
                        },

                        onDifficultySelected = { selectedDifficulty ->
                            setup = setup.copy(difficulty = selectedDifficulty)
                        },

                        onGridSizeSelected = { selectedGridSize ->
                            setup = setup.copy(gridSize = selectedGridSize)
                        },

                        onPressureTypeSelected = { selectedPressureType ->
                            setup = setup.copy(pressureType = selectedPressureType)
                        },

                        onStartGameClick = {
                            startGame()
                        },

                        onBackClick = {
                            finish()
                        },

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun createPuzzle(gridSize: GridSize): Puzzle {

        val generator = KakuroGenerator()

        val grid = generator.generate(
            gridSize = setup.gridSize,
            difficulty = setup.difficulty
        )

        if (grid == null) {
            println("Failed to generate puzzle.")
        } else {
            println("Puzzle generated successfully.")
            println(grid)
        }

        val manualGrid = KakuroGrid(gridSize).generateGrid()


        return Puzzle(
            grid = grid ?: manualGrid
        )
    }

    private fun startGame() {
        val pressureType = if (setup.gameMode == GameMode.CHALLENGING) {
            setup.pressureType ?: PressureType.TIME_BASED
        } else {
            null
        }

        val hintConstraint = if (setup.gameMode == GameMode.CHALLENGING) {
            HintConstraints.getRule(setup.difficulty)
        } else {
            HintConstraint(0, 0, 0)
        }

        val puzzle = createPuzzle(setup.gridSize)

        val gameSession = GameSession(
            puzzle = puzzle,
            gridSize = setup.gridSize,
            result = GameResult.IN_PROGRESS,
            mistakes = 0,
            gameMode = setup.gameMode,
            pressureType = pressureType,
            difficulty = setup.difficulty,
            score = 0,
            remainingTimeSeconds = createRemainingTimeSeconds(
                gameMode = setup.gameMode,
                pressureType = pressureType,
                difficulty = setup.difficulty
            ),
            maxMistakes = createMaxMistakes(
                gameMode = setup.gameMode,
                pressureType = pressureType,
                difficulty = setup.difficulty
            ),
            remainingCellHints = hintConstraint.cellRevealCount,
            remainingRowHints = hintConstraint.rowRevealCount,
            remainingColumnHints = hintConstraint.columnRevealCount,
            startTime = System.currentTimeMillis()
        )

        navigateToGame(gameSession)
    }


    private fun createRemainingTimeSeconds(
        gameMode: GameMode,
        pressureType: PressureType?,
        difficulty: Difficulty
    ): Int? {
        if (gameMode != GameMode.CHALLENGING || pressureType == PressureType.MISTAKE_LIMIT) {
            return null
        }

        val challengeConstraints = ChallengeConstraints
        return challengeConstraints.getRule(pressureType, difficulty).timeSeconds
    }

    private fun createMaxMistakes(
        gameMode: GameMode,
        pressureType: PressureType?,
        difficulty: Difficulty
    ): Int? {
        if (gameMode != GameMode.CHALLENGING) {
            return null
        }

        val challengeConstraints = ChallengeConstraints
        return challengeConstraints.getRule(pressureType, difficulty).maxMistakes
    }

    private fun navigateToGame(gameSession: GameSession) {
        GameSessionHolder.currentSession = gameSession

        startActivity(
            Intent(this, GameActivity::class.java)
        )
    }
}
