package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Direction
import com.example.kakuro_hero.models.Layout

/**
 * Validates that a layouts forms a valid Kakuro Grid.
 */
class LayoutValidator {

    // Checks whether the layout is valid for a Kakuro puzzle.
    // Each playable cell must belong to: one horizontal run or one vertical run
    fun isValidLayout(layout: Layout): Boolean {
        for (r in 0 until layout.size) {
            for (c in 0 until layout.size) {
                if (!layout.playable[r][c]) continue

                val horizontalLength = runLength(layout, r, c, Direction.HORIZONTAL)
                val verticalLength = runLength(layout, r, c, Direction.VERTICAL)

                if (horizontalLength !in 2..9) return false
                if (verticalLength !in 2..9) return false
            }
        }

        return true
    }

    private fun runLength(
        layout: Layout,
        row: Int,
        col: Int,
        direction: Direction
    ): Int {
        var startRow = row
        var startCol = col

        while (true) {
            val prevRow = if (direction == Direction.VERTICAL) startRow - 1 else startRow
            val prevCol = if (direction == Direction.HORIZONTAL) startCol - 1 else startCol

            if (prevRow < 0 || prevCol < 0) break
            if (!layout.playable[prevRow][prevCol]) break

            startRow = prevRow
            startCol = prevCol
        }

        var currentRow = startRow
        var currentCol = startCol
        var length = 0

        while (
            currentRow in 0 until layout.size &&
            currentCol in 0 until layout.size &&
            layout.playable[currentRow][currentCol]
        ) {
            length++

            if (direction == Direction.HORIZONTAL) {
                currentCol++
            } else {
                currentRow++
            }
        }

        return length
    }
}