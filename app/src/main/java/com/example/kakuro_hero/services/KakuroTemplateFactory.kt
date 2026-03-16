package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.KakuroTemplate

/**
 * Creates predefined Kakuro layout templates.
 */
class KakuroTemplateFactory : IKakuroTemplateFactory {

    override fun createTemplates(size: Int, difficulty: Difficulty): List<KakuroTemplate> {
        return when (size) {
            4 -> templates4(difficulty)
            5 -> templates5(difficulty)
            6 -> templates6(difficulty)
            7 -> templates7(difficulty)
            else -> emptyList()
        }
    }

    private fun templates4(difficulty: Difficulty): List<KakuroTemplate> {
        val t1 = KakuroTemplate(
            size = 4,
            playable = listOf(
                listOf(false, false, false, false),
                listOf(false, true,  true,  false),
                listOf(false, true,  true,  true),
                listOf(false, false, true,  true)
            )
        )

        val t2 = KakuroTemplate(
            size = 4,
            playable = listOf(
                listOf(false, false, false, false),
                listOf(false, true,  true,  false),
                listOf(false, true,  true,  false),
                listOf(false, true,  true,  false)
            )
        )

        return when (difficulty) {
            Difficulty.EASY -> listOf(t1, t2)
            Difficulty.MEDIUM -> listOf(t1, t2)
            Difficulty.HARD -> listOf(t2, t1)
            Difficulty.EXPERT -> listOf(t2)
        }
    }

    private fun templates5(difficulty: Difficulty): List<KakuroTemplate> {
        val t1 = KakuroTemplate(
            size = 5,
            playable = listOf(
                listOf(false, false, false, false, false),
                listOf(false, true,  true,  false, false),
                listOf(false, true,  true,  true,  false),
                listOf(false, true,  true,  true,  false),
                listOf(false, false, true,  true,  false)
            )
        )

        val t2 = KakuroTemplate(
            size = 5,
            playable = listOf(
                listOf(false, false, false, false, false),
                listOf(false, true,  true,  true,  false),
                listOf(false, true,  true,  true,  false),
                listOf(false, false, true,  true,  true),
                listOf(false, false, true,  true,  true)
            )
        )

        val t3 = KakuroTemplate(
            size = 5,
            playable = listOf(
                listOf(false, false, false, false, false),
                listOf(false, true,  true,  false, false),
                listOf(false, true,  true,  true,  false),
                listOf(false, false, true,  true,  true),
                listOf(false, false, true,  true,  true)
            )
        )

        return when (difficulty) {
            Difficulty.EASY -> listOf(t1, t2)
            Difficulty.MEDIUM -> listOf(t1, t2, t3)
            Difficulty.HARD -> listOf(t2, t3, t1)
            Difficulty.EXPERT -> listOf(t3, t2)
        }
    }

    private fun templates6(difficulty: Difficulty): List<KakuroTemplate> {
        val t1 = KakuroTemplate(
            size = 6,
            playable = listOf(
                listOf(false, false, false, false, false, false),
                listOf(false, true,  true,  true,  false, false),
                listOf(false, true,  true,  true,  false, false),
                listOf(false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true)
            )
        )

        val t2 = KakuroTemplate(
            size = 6,
            playable = listOf(
                listOf(false, false, false, false, false, false),
                listOf(false, true,  true,  false, false, false),
                listOf(false, true,  true,  true,  false, false),
                listOf(false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true)
            )
        )

        val t3 = KakuroTemplate(
            size = 6,
            playable = listOf(
                listOf(false, false, false, false, false, false),
                listOf(false, true,  true,  true,  false, false),
                listOf(false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true)
            )
        )

        return when (difficulty) {
            Difficulty.EASY -> listOf(t1, t2)
            Difficulty.MEDIUM -> listOf(t1, t2, t3)
            Difficulty.HARD -> listOf(t2, t3, t1)
            Difficulty.EXPERT -> listOf(t3, t2)
        }
    }

    private fun templates7(difficulty: Difficulty): List<KakuroTemplate> {
        val t1 = KakuroTemplate(
            size = 7,
            playable = listOf(
                listOf(false, false, false, false, false, false, false),
                listOf(false, true,  true,  true,  false, false, false),
                listOf(false, true,  true,  true,  true,  false, false),
                listOf(false, true,  true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true,  true)
            )
        )

        val t2 = KakuroTemplate(
            size = 7,
            playable = listOf(
                listOf(false, false, false, false, false, false, false),
                listOf(false, true,  true,  false, false, false, false),
                listOf(false, true,  true,  true,  false, false, false),
                listOf(false, true,  true,  true,  true,  false, false),
                listOf(false, false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true,  true)
            )
        )

        val t3 = KakuroTemplate(
            size = 7,
            playable = listOf(
                listOf(false, false, false, false, false, false, false),
                listOf(false, true,  true,  true,  false, false, false),
                listOf(false, true,  true,  true,  true,  false, false),
                listOf(false, false, true,  true,  true,  true,  false),
                listOf(false, false, true,  true,  true,  true,  false),
                listOf(false, false, false, true,  true,  true,  true),
                listOf(false, false, false, true,  true,  true,  true)
            )
        )

        return when (difficulty) {
            Difficulty.EASY -> listOf(t1, t2)
            Difficulty.MEDIUM -> listOf(t1, t2, t3)
            Difficulty.HARD -> listOf(t2, t3, t1)
            Difficulty.EXPERT -> listOf(t3, t2)
        }
    }
}