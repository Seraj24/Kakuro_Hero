package com.example.kakuro_hero.controllers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.kakuro_hero.models.CellPosition
import com.example.kakuro_hero.models.GameMode
import com.example.kakuro_hero.models.GameResult
import com.example.kakuro_hero.models.HintType
import com.example.kakuro_hero.models.KakuroPlayableCell
import com.example.kakuro_hero.models.PressureType
import com.example.kakuro_hero.services.GameSessionHolder
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme
import com.example.kakuro_hero.views.GameView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : ComponentActivity() {

    private var gameSession by mutableStateOf(
        GameSessionHolder.currentSession ?: error("GameSession is missing.")
    )

    private var selectedCell by mutableStateOf<CellPosition?>(null)

    private var showMistakes by mutableStateOf<Boolean>(false)

    private var gameEnded by mutableStateOf<Boolean>(false)

    private var hintedCells by mutableStateOf<Set<CellPosition>>(emptySet())

    private var resultReady by mutableStateOf(false)
    private var countdownSeconds by mutableIntStateOf(5)
    private var remainingSeconds by mutableIntStateOf(0)

    private var sessionStartTimeMillis: Long = 0L

    private var isPaused by mutableStateOf(false)
    private var pauseStartedTimeMillis: Long? = null
    private var totalPausedDurationMillis: Long = 0L
    private var message by mutableStateOf<String?>(null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sessionStartTimeMillis = System.currentTimeMillis()

        initializeChallengeTimer()
        startChallengeTimerIfNeeded()

        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    GameView().GameUI(
                        puzzle = gameSession.puzzle,
                        selectedCell = selectedCell,
                        onCellClick = { position ->
                            selectCell(position)
                        },
                        onNumberClick = { number ->
                            enterNumber(number)
                        },
                        onClearClick = {
                            clearSelectedCell()
                        },
                        onClearAllClick = {
                            clearAll()
                        },
                        onCheckClick = {
                            checkSolution()
                        },
                        onBackClick = {
                            finish()
                        },
                        onPauseClick = {
                            togglePause()
                        },
                        onFinishClick = {
                            endGame()
                        },
                        onViewResultsClick = {
                            navigateToGameResult()
                        },
                        onCellHintClick = {
                            useHint(HintType.CELL_REVEAL)
                        },
                        onRowHintClick = {
                            useHint(HintType.ROW_REVEAL)
                        },
                        onColumnHintClick = {
                            useHint(HintType.COLUMN_REVEAL)
                        },
                        remainingCellHints = gameSession.remainingCellHints,
                        remainingRowHints = gameSession.remainingRowHints,
                        remainingColumnHints = gameSession.remainingColumnHints,
                        hintedCells = hintedCells,
                        showMistakes = showMistakes,
                        gameEnded = gameEnded,
                        isPaused = isPaused,
                        resultReady = resultReady,
                        countdownSeconds = countdownSeconds,
                        isChallengingMode = isChallengingMode(),
                        challengeType = gameSession.pressureType,
                        mistakeCount = gameSession.mistakes,
                        maxMistakes = getMaxMistakes(),
                        remainingSeconds = remainingSeconds,
                        modifier = Modifier.padding(innerPadding),
                        message = message
                    )
                }
            }
        }
    }

    private fun isChallengingMode(): Boolean {
        return gameSession.gameMode == GameMode.CHALLENGING
    }

    private fun getMaxMistakes(): Int? {
        return gameSession.maxMistakes
    }

    private fun initializeChallengeTimer() {
        if (
            gameSession.pressureType == PressureType.TIME_BASED ||
            gameSession.pressureType == PressureType.KAKURO_HERO
        ) {
            remainingSeconds = gameSession.remainingTimeSeconds ?: 0
        }
    }

    private fun startChallengeTimerIfNeeded() {

        if (!isChallengingMode()) return

        if (
            gameSession.pressureType != PressureType.TIME_BASED &&
            gameSession.pressureType != PressureType.KAKURO_HERO
        ) return

        lifecycleScope.launch {
            while (!gameEnded && remainingSeconds > 0) {
                delay(1000)

                if (isPaused) continue

                remainingSeconds--
            }

            if (!gameEnded && remainingSeconds <= 0) {
                message = "Time is up."
                endGame()
            }
        }
    }

    private fun togglePause() {
        if (gameEnded) return

        if (isPaused) {
            resumeGame()
        } else {
            pauseGame()
        }
    }

    private fun pauseGame() {
        if (gameEnded || isPaused) return

        isPaused = true
        pauseStartedTimeMillis = System.currentTimeMillis()
        message = "Game paused."
    }

    private fun resumeGame() {
        if (gameEnded || !isPaused) return

        val pausedAt = pauseStartedTimeMillis ?: System.currentTimeMillis()
        totalPausedDurationMillis += System.currentTimeMillis() - pausedAt
        pauseStartedTimeMillis = null
        isPaused = false
        message = null
    }



    private fun selectCell(position: CellPosition) {
        val cell = gameSession.puzzle.grid.getCell(position.row, position.column)

        if (cell is KakuroPlayableCell) {
            selectedCell = position
            message = null
        }
    }

    private fun enterNumber(number: Int) {

        if (gameEnded || isPaused) return

        val position = selectedCell ?: run {
            message = "Select a cell first."
            return
        }

        val cell = gameSession.puzzle.grid.getCell(position.row, position.column)

        if (cell is KakuroPlayableCell) {
            val hasError = number != cell.solution

            val updatedCell = cell.copy(
                userAnswer = number,
                hasError = hasError
            )

            val updatedGrid = gameSession.puzzle.grid.setCell(
                row = position.row,
                column = position.column,
                newCell = updatedCell
            )

            val updatedPuzzle = gameSession.puzzle.copy(grid = updatedGrid)

            val newMistakes =
                if (hasError) gameSession.mistakes + 1
                else gameSession.mistakes

            gameSession = gameSession.copy(
                puzzle = updatedPuzzle,
                mistakes = newMistakes
            )

            if (
                isChallengingMode() &&
                (gameSession.pressureType == PressureType.MISTAKE_LIMIT ||
                        gameSession.pressureType == PressureType.KAKURO_HERO)
            ) {
                val maxMistakes = gameSession.maxMistakes ?: 0
                if (gameSession.mistakes >= maxMistakes) {
                    message = "You reached the mistake limit."
                    endGame()
                    return
                }
            }

            message = null
        }
    }

    private fun clearSelectedCell() {

        if (gameEnded || isPaused) return

        val position = selectedCell ?: run {
            message = "Select a cell first."
            return
        }

        val cell = gameSession.puzzle.grid.getCell(position.row, position.column)

        if (cell is KakuroPlayableCell) {
            val updatedCell = cell.copy(
                userAnswer = null,
                hasError = false
            )

            val updatedGrid = gameSession.puzzle.grid.setCell(
                row = position.row,
                column = position.column,
                newCell = updatedCell
            )

            val updatedPuzzle = gameSession.puzzle.copy(grid = updatedGrid)

            gameSession = gameSession.copy(
                puzzle = updatedPuzzle
            )

            message = null
        }
    }

    private fun checkSolution() {
        if (gameEnded || isPaused) return

        showMistakes = !showMistakes
    }

    private fun highlightHintedCellsTemporarily(cells: Set<CellPosition>) {
        hintedCells = hintedCells + cells

        lifecycleScope.launch {
            delay(5000)
            hintedCells = hintedCells - cells
        }
    }


    private fun useHint(hintType: HintType) {
        if (gameEnded || isPaused) return

        if (!isChallengingMode()) {
            message = "Hints are only available in challenging mode."
            return
        }

        val position = selectedCell ?: run {
            message = "Select a cell first."
            return
        }

        when (hintType) {
            HintType.CELL_REVEAL -> useCellRevealHint(position)
            HintType.ROW_REVEAL -> useRowRevealHint(position)
            HintType.COLUMN_REVEAL -> useColumnRevealHint(position)
        }
    }

    private fun useCellRevealHint(position: CellPosition) {
        if (gameSession.remainingCellHints <= 0) {
            return
        }

        val cell = gameSession.puzzle.grid.getCell(position.row, position.column)

        if (cell !is KakuroPlayableCell) {
            return
        }

        if (cell.userAnswer == cell.solution) {
            return
        }

        val updatedCell = cell.copy(
            userAnswer = cell.solution,
            hasError = false
        )

        val updatedGrid = gameSession.puzzle.grid.setCell(
            row = position.row,
            column = position.column,
            newCell = updatedCell
        )

        gameSession = gameSession.copy(
            puzzle = gameSession.puzzle.copy(grid = updatedGrid),
            remainingCellHints = gameSession.remainingCellHints - 1
        )

        highlightHintedCellsTemporarily(setOf(position))
        message = null
    }

    private fun useRowRevealHint(position: CellPosition) {
        if (gameSession.remainingRowHints <= 0) {
            return
        }

        var updatedGrid = gameSession.puzzle.grid
        val revealedPositions = mutableSetOf<CellPosition>()

        gameSession.puzzle.grid.cells[position.row].forEachIndexed { columnIndex, cell ->
            if (cell is KakuroPlayableCell) {
                val updatedCell = cell.copy(
                    userAnswer = cell.solution,
                    hasError = false
                )

                updatedGrid = updatedGrid.setCell(
                    row = position.row,
                    column = columnIndex,
                    newCell = updatedCell
                )

                revealedPositions.add(CellPosition(position.row, columnIndex))
            }
        }

        gameSession = gameSession.copy(
            puzzle = gameSession.puzzle.copy(grid = updatedGrid),
            remainingRowHints = gameSession.remainingRowHints - 1
        )

        highlightHintedCellsTemporarily(revealedPositions)
        message = null
    }

    private fun useColumnRevealHint(position: CellPosition) {
        if (gameSession.remainingColumnHints <= 0) {
            return
        }

        var updatedGrid = gameSession.puzzle.grid
        val revealedPositions = mutableSetOf<CellPosition>()

        gameSession.puzzle.grid.cells.forEachIndexed { rowIndex, row ->
            val cell = row[position.column]

            if (cell is KakuroPlayableCell) {
                val updatedCell = cell.copy(
                    userAnswer = cell.solution,
                    hasError = false
                )

                updatedGrid = updatedGrid.setCell(
                    row = rowIndex,
                    column = position.column,
                    newCell = updatedCell
                )

                revealedPositions.add(CellPosition(rowIndex, position.column))
            }
        }

        gameSession = gameSession.copy(
            puzzle = gameSession.puzzle.copy(grid = updatedGrid),
            remainingColumnHints = gameSession.remainingColumnHints - 1
        )

        highlightHintedCellsTemporarily(revealedPositions)
        message = null
    }

    private fun endGame() {
        if (gameEnded) return

        if (isPaused) {
            resumeGame()
        }

        showMistakes = true
        gameEnded = true

        val solved = isPuzzleSolved()

        val elapsedSeconds =
            (
                    (System.currentTimeMillis() - sessionStartTimeMillis - totalPausedDurationMillis) / 1000
                    ).toInt()

        gameSession = gameSession.copy(
            result = if (solved) GameResult.WIN else GameResult.LOSE,
            sessionTime = elapsedSeconds
        )

        resultReady = true
    }

    private fun isPuzzleSolved(): Boolean {
        return gameSession.puzzle.grid.cells.all { row ->
            row.all { cell ->
                when (cell) {
                    is KakuroPlayableCell -> cell.userAnswer == cell.solution
                    else -> true
                }
            }
        }
    }

    private fun clearAll() {
        if (gameEnded || isPaused) return

        var updatedGrid = gameSession.puzzle.grid

        gameSession.puzzle.grid.cells.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell is KakuroPlayableCell && cell.userAnswer != null) {
                    val updatedCell = cell.copy(
                        userAnswer = null,
                        hasError = false
                    )

                    updatedGrid = updatedGrid.setCell(
                        row = rowIndex,
                        column = columnIndex,
                        newCell = updatedCell
                    )
                }
            }
        }

        val updatedPuzzle = gameSession.puzzle.copy(grid = updatedGrid)

        gameSession = gameSession.copy(
            puzzle = updatedPuzzle
        )

        selectedCell = null
        message = null
    }


    private fun startResultCountdown() {
        resultReady = false
        countdownSeconds = 5

        lifecycleScope.launch {
            while (countdownSeconds > 0) {
                delay(1000)
                countdownSeconds--
            }
            resultReady = true
        }
    }

    private fun navigateToGameResult() {

        GameSessionHolder.currentSession = gameSession

        val intent = Intent(this, GameResultActivity::class.java)

        startActivity(intent)
        finish()
    }



}