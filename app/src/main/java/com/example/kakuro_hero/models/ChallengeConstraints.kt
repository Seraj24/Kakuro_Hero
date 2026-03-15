package com.example.kakuro_hero.models

object ChallengeConstraints {

    fun getRule(
        pressureType: PressureType?,
        difficulty: Difficulty
    ): ChallengeConstraint {
        return when (pressureType) {
            PressureType.TIME_BASED -> when (difficulty) {
                Difficulty.EASY -> ChallengeConstraint(timeSeconds = 600)
                Difficulty.MEDIUM -> ChallengeConstraint(timeSeconds = 480)
                Difficulty.HARD -> ChallengeConstraint(timeSeconds = 360)
                Difficulty.EXPERT -> ChallengeConstraint(timeSeconds = 240)
            }

            PressureType.MISTAKE_LIMIT -> when (difficulty) {
                Difficulty.EASY -> ChallengeConstraint(maxMistakes = 5)
                Difficulty.MEDIUM -> ChallengeConstraint(maxMistakes = 4)
                Difficulty.HARD -> ChallengeConstraint(maxMistakes = 3)
                Difficulty.EXPERT -> ChallengeConstraint(maxMistakes = 2)
            }

            PressureType.KAKURO_HERO -> when (difficulty) {
                Difficulty.EASY -> ChallengeConstraint(timeSeconds = 600, maxMistakes = 3)
                Difficulty.MEDIUM -> ChallengeConstraint(timeSeconds = 480, maxMistakes = 2)
                Difficulty.HARD -> ChallengeConstraint(timeSeconds = 360, maxMistakes = 2)
                Difficulty.EXPERT -> ChallengeConstraint(timeSeconds = 240, maxMistakes = 1)
            }

            null -> ChallengeConstraint()
        }
    }

    fun getAllRulesForPressureType(
        pressureType: PressureType
    ): Map<Difficulty, ChallengeConstraint> {
        return Difficulty.entries.associateWith { difficulty ->
            getRule(pressureType, difficulty)
        }
    }
}