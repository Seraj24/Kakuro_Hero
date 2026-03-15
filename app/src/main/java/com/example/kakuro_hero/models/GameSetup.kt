package com.example.kakuro_hero.models


enum class GameMode {
    RELAXING,
    CHALLENGING
}

enum class PressureType {
    TIME_BASED,
    MISTAKE_LIMIT,
    KAKURO_HERO
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
    EXPERT
}


data class GameSetup(
    val gameMode: GameMode = GameMode.RELAXING,
    val gridSize: GridSize = GridSize.FIVE,
    val difficulty: Difficulty = Difficulty.EASY,
    val pressureType: PressureType? = null
)
