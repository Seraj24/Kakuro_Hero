package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Direction
import com.example.kakuro_hero.models.Layout
import com.example.kakuro_hero.models.Pos
import com.example.kakuro_hero.models.Run

/**
 * Extracts horizontal and vertical runs from a layout.
 */
class RunFinder {

    fun findRuns(layout: Layout): List<Run> {
        val runs = mutableListOf<Run>()
        var nextRunId = 0

        // Horizontal runs
        for (r in 0 until layout.size) {
            var c = 0
            while (c < layout.size) {
                val isStart = layout.playable[r][c] && (c == 0 || !layout.playable[r][c - 1])

                if (!isStart) {
                    c++
                    continue
                }

                val cells = mutableListOf<Pos>()
                var currentCol = c

                while (currentCol < layout.size && layout.playable[r][currentCol]) {
                    cells += Pos(r + 1, currentCol + 1)
                    currentCol++
                }

                if (cells.size >= 2) {
                    runs += Run(
                        id = nextRunId++,
                        direction = Direction.HORIZONTAL,
                        origin = Pos(r + 1, c),
                        cells = cells
                    )
                }

                c = currentCol
            }
        }

        // Vertical runs
        for (c in 0 until layout.size) {
            var r = 0
            while (r < layout.size) {
                val isStart = layout.playable[r][c] && (r == 0 || !layout.playable[r - 1][c])

                if (!isStart) {
                    r++
                    continue
                }

                val cells = mutableListOf<Pos>()
                var currentRow = r

                while (currentRow < layout.size && layout.playable[currentRow][c]) {
                    cells += Pos(currentRow + 1, c + 1)
                    currentRow++
                }

                if (cells.size >= 2) {
                    runs += Run(
                        id = nextRunId++,
                        direction = Direction.VERTICAL,
                        origin = Pos(r, c + 1),
                        cells = cells
                    )
                }

                r = currentRow
            }
        }

        return runs
    }
}