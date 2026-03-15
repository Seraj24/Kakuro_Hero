package com.example.kakuro_hero.models

data class Puzzle(
    val grid: KakuroGrid
) {
    fun generateGrid(): Puzzle {
        return copy(grid = grid.generateGrid())
    }
}