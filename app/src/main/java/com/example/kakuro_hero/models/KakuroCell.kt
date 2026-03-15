package com.example.kakuro_hero.models

sealed class KakuroCell

data class KakuroClueCell(
    val rightSum: Int?,
    val downSum: Int?
) : KakuroCell()

data class KakuroPlayableCell(
    val solution: Int,
    val userAnswer: Int?,
    val hasError: Boolean
) : KakuroCell()

data object KakuroStaticCell : KakuroCell()


