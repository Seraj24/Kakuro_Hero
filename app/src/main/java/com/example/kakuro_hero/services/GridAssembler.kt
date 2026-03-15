package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.GridSize
import com.example.kakuro_hero.models.KakuroCell
import com.example.kakuro_hero.models.KakuroClueCell
import com.example.kakuro_hero.models.KakuroGrid
import com.example.kakuro_hero.models.KakuroPlayableCell
import com.example.kakuro_hero.models.KakuroStaticCell
import com.example.kakuro_hero.models.Layout
import com.example.kakuro_hero.models.Pos
import com.example.kakuro_hero.models.RunClue

/**
 * Builds the final KakuroGrid using the layout structure,
 * the generated clue sums, and the solution digits.
 * Converts them into KakuroCell objects (static, clue, and playable).
 */
class GridAssembler {

    fun assembleGrid(
        gridSize: GridSize,
        layout: Layout,
        clues: Map<Pos, RunClue>,
        solution: Map<Pos, Int>
    ): KakuroGrid {
        val size = layout.size

        val cells = MutableList(size + 1) {
            MutableList<KakuroCell>(size + 1) { KakuroStaticCell }
        }

        for ((pos, clue) in clues) {
            cells[pos.row][pos.col] = KakuroClueCell(
                rightSum = clue.rightSum,
                downSum = clue.downSum
            )
        }

        for (r in 0 until size) {
            for (c in 0 until size) {
                if (!layout.playable[r][c]) continue

                val pos = Pos(r + 1, c + 1)
                val digit = solution[pos] ?: error("Missing solution at $pos")

                cells[pos.row][pos.col] = KakuroPlayableCell(
                    solution = digit,
                    userAnswer = null,
                    hasError = false
                )
            }
        }

        return KakuroGrid(
            gridSize = gridSize,
            cells = cells
        )
    }
}