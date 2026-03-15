package com.example.kakuro_hero.models




enum class GameResult {
    IN_PROGRESS,
    WIN,
    LOSE
}

data class GameSession(
    val puzzle: Puzzle,
    val gridSize: GridSize,
    val result: GameResult,
    val mistakes: Int,
    val gameMode: GameMode,
    val pressureType: PressureType?,
    val difficulty: Difficulty,
    val score: Int = 0,
    val remainingTimeSeconds: Int? = null,
    val maxMistakes: Int? = null,
    val remainingCellHints: Int = 0,
    val remainingRowHints: Int = 0,
    val remainingColumnHints: Int = 0,
    val sessionTime: Int = 0,
    val startTime: Long
)