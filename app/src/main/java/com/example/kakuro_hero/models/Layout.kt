package com.example.kakuro_hero.models

// Defines the shape by size and which cells are Playable and which are Static
data class Layout(
    val size: Int,
    val playable: List<List<Boolean>>
)
