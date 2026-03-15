package com.example.kakuro_hero.models

object HintConstraints {

    fun getRule(difficulty: Difficulty): HintConstraint {
        return when (difficulty) {
            Difficulty.EASY -> HintConstraint(
                cellRevealCount = 3,
                rowRevealCount = 2,
                columnRevealCount = 2
            )

            Difficulty.MEDIUM -> HintConstraint(
                cellRevealCount = 2,
                rowRevealCount = 1,
                columnRevealCount = 1
            )

            Difficulty.HARD -> HintConstraint(
                cellRevealCount = 1,
                rowRevealCount = 1,
                columnRevealCount = 0
            )

            Difficulty.EXPERT -> HintConstraint(
                cellRevealCount = 1,
                rowRevealCount = 0,
                columnRevealCount = 0
            )
        }
    }
}
