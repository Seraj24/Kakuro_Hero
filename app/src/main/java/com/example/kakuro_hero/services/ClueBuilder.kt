package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Direction
import com.example.kakuro_hero.models.Pos
import com.example.kakuro_hero.models.Run
import com.example.kakuro_hero.models.RunClue

/**
 * Builds clue sums for each run using the generated solution digits.
 * Each run's digits are summed and stored as a horizontal (right)
 * or vertical (down) clue at the run's origin cell.
 */
class ClueBuilder {

    fun buildClues(
        runs: List<Run>,
        solution: Map<Pos, Int>
    ): Map<Pos, RunClue> {
        val clues = mutableMapOf<Pos, RunClue>()

        for (run in runs) {
            val sum = run.cells.sumOf { cell ->
                solution[cell] ?: error("Missing solution digit for $cell")
            }

            val currentClue = clues[run.origin] ?: RunClue()

            clues[run.origin] = when (run.direction) {
                Direction.HORIZONTAL -> currentClue.copy(rightSum = sum)
                Direction.VERTICAL -> currentClue.copy(downSum = sum)
            }
        }

        return clues
    }
}