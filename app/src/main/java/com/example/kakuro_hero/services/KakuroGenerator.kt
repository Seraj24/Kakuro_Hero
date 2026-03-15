package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.GridSize
import com.example.kakuro_hero.models.KakuroGrid
import com.example.kakuro_hero.models.Layout
import kotlin.random.Random

/**
 * Coordinates the Kakuro puzzle generation process.
 */
class KakuroGenerator(
    private val random: Random = Random.Default,
    private val templateFactory: IKakuroTemplateFactory = KakuroTemplateFactory(),
    private val layoutValidator: LayoutValidator = LayoutValidator(),
    private val runFinder: RunFinder = RunFinder(),
    private val solutionGenerator: SolutionGenerator = SolutionGenerator(random),
    private val clueBuilder: ClueBuilder = ClueBuilder(),
    private val gridAssembler: GridAssembler = GridAssembler()
) {

    fun generate(
        gridSize: GridSize,
        difficulty: Difficulty
    ): KakuroGrid? {
        val templates = templateFactory
            .createTemplates(gridSize.size, difficulty)
            .map { Layout(it.size, it.playable) }
            .filter { layoutValidator.isValidLayout(it) }
            .shuffled(random)

        if (templates.isEmpty()) {
            println("No valid templates found for size=${gridSize.size}, difficulty=$difficulty")
            return null
        }

        for ((index, layout) in templates.withIndex()) {
            println("Trying template ${index + 1}")

            val runs = runFinder.findRuns(layout)
            if (runs.isEmpty()) {
                println("Template ${index + 1}: no runs found")
                continue
            }

            val solution = solutionGenerator.generateSolution(layout, runs, difficulty)
            if (solution == null) {
                println("Template ${index + 1}: failed to generate solution")
                continue
            }

            val clues = clueBuilder.buildClues(runs, solution)

            println("Template ${index + 1}: success")

            return gridAssembler.assembleGrid(
                gridSize = gridSize,
                layout = layout,
                clues = clues,
                solution = solution
            )
        }

        println("Failed to generate puzzle for size=${gridSize.size}, difficulty=$difficulty")
        return null
    }

    // =========================================================
    // LAYOUT VALIDATION
    // =========================================================



    // =========================================================
    // RUN SCANNING
    // =========================================================



    // =========================================================
    // SOLUTION GENERATION
    // =========================================================



    // =========================================================
    // CLUES
    // =========================================================


    // =========================================================
    // GRID ASSEMBLY
    // =========================================================


}