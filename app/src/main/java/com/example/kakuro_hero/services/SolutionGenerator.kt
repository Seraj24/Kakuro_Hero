package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.CellLinks
import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.Direction
import com.example.kakuro_hero.models.Layout
import com.example.kakuro_hero.models.Pos
import com.example.kakuro_hero.models.Run
import kotlin.random.Random

/**
 * Generates a valid digit solution by trying different combinations
 * until the following rules are met:
 * - digits must be between 1 and 9
 * - digits cannot repeat within the same run
 */
class SolutionGenerator(
    private val random: Random = Random.Default
) {

    fun generateSolution(
        layout: Layout,
        runs: List<Run>,
        difficulty: Difficulty
    ): Map<Pos, Int>? {
        val linksByCell = buildCellLinks(runs)
        val allPlayableCells = linksByCell.keys.toList()

        val assignment = mutableMapOf<Pos, Int>()

        val success = solveCells(
            cells = allPlayableCells,
            linksByCell = linksByCell,
            assignment = assignment,
            difficulty = difficulty
        )

        return if (success) assignment.toMap() else null
    }

    private fun solveCells(
        cells: List<Pos>,
        linksByCell: Map<Pos, CellLinks>,
        assignment: MutableMap<Pos, Int>,
        difficulty: Difficulty
    ): Boolean {
        if (assignment.size == cells.size) {
            return true
        }

        val selection = selectNextCell(cells, linksByCell, assignment, difficulty) ?: return false

        val nextCell = selection.first
        val possibleDigits = selection.second

        for (digit in possibleDigits) {
            assignment[nextCell] = digit

            val solved = solveCells(cells, linksByCell, assignment, difficulty)
            if (solved) {
                return true
            }

            assignment.remove(nextCell)
        }

        return false
    }

    private fun selectNextCell(
        cells: List<Pos>,
        linksByCell: Map<Pos, CellLinks>,
        assignment: Map<Pos, Int>,
        difficulty: Difficulty
    ): Pair<Pos, List<Int>>? {
        val availableChoices = mutableListOf<Pair<Pos, List<Int>>>()

        for (cell in cells) {
            if (cell in assignment) {
                continue
            }

            val possibleDigits = possibleDigits(cell, linksByCell, assignment, difficulty)
            if (possibleDigits.isEmpty()) {
                continue
            }

            availableChoices.add(cell to possibleDigits)
        }

        if (availableChoices.isEmpty()) {
            return null
        }

        availableChoices.sortBy { it.second.size }

        val numberOfBestChoices = minOf(3, availableChoices.size)
        val bestChoices = availableChoices.take(numberOfBestChoices)

        return bestChoices.random(random)
    }

    private fun possibleDigits(
        pos: Pos,
        linksByCell: Map<Pos, CellLinks>,
        assignment: Map<Pos, Int>,
        difficulty: Difficulty
    ): List<Int> {
        val links = linksByCell[pos] ?: return emptyList()

        val usedDigits = mutableSetOf<Int>()

        links.horizontalRunId?.let { runId ->
            for ((cell, cellLinks) in linksByCell) {
                if (cell == pos) continue
                if (cellLinks.horizontalRunId == runId) {
                    assignment[cell]?.let(usedDigits::add)
                }
            }
        }

        links.verticalRunId?.let { runId ->
            for ((cell, cellLinks) in linksByCell) {
                if (cell == pos) continue
                if (cellLinks.verticalRunId == runId) {
                    assignment[cell]?.let(usedDigits::add)
                }
            }
        }

        val digits = (1..9)
            .filterNot { it in usedDigits }
            .shuffled(random)

        return when (difficulty) {
            Difficulty.EASY -> digits.sorted()
            Difficulty.MEDIUM -> digits
            Difficulty.HARD -> digits.sortedDescending()
            Difficulty.EXPERT -> digits.sortedDescending()
        }
    }

    private fun buildCellLinks(runs: List<Run>): Map<Pos, CellLinks> {
        val horizontalByCell = mutableMapOf<Pos, Int>()
        val verticalByCell = mutableMapOf<Pos, Int>()

        for (run in runs) {
            for (cell in run.cells) {
                when (run.direction) {
                    Direction.HORIZONTAL -> horizontalByCell[cell] = run.id
                    Direction.VERTICAL -> verticalByCell[cell] = run.id
                }
            }
        }

        val allCells = (horizontalByCell.keys + verticalByCell.keys).toSet()

        return allCells.associateWith { pos ->
            CellLinks(
                horizontalRunId = horizontalByCell[pos],
                verticalRunId = verticalByCell[pos]
            )
        }
    }
}