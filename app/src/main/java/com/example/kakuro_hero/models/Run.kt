package com.example.kakuro_hero.models

// Group of cells controlled by one clue
data class Run(
    val id: Int,
    val direction: Direction,
    val origin: Pos,
    val cells: List<Pos>
)
