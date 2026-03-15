package com.example.kakuro_hero.models


data class KakuroGrid(
    val gridSize: GridSize,
    val cells: List<List<KakuroCell>> = emptyList()
) {
    fun getCell(row: Int, column: Int): KakuroCell {
        return cells[row][column]
    }

    fun setCell(row: Int, column: Int, newCell: KakuroCell): KakuroGrid {
        val newRows = cells.mapIndexed { rowIndex, currentRow ->
            currentRow.mapIndexed { columnIndex, cell ->
                if (rowIndex == row && columnIndex == column) {
                    newCell
                } else {
                    cell
                }
            }
        }

        return copy(cells = newRows)
    }

    // Manually generated grid for now
    fun generateGrid(): KakuroGrid  {
        return copy(cells = generatedRows())
    }

    fun generatedRows(): List<List<KakuroCell>> {
        return listOf(
            listOf(
                KakuroStaticCell,
                KakuroClueCell(downSum = 4, rightSum = null),
                KakuroClueCell(downSum = 7, rightSum = null),
                KakuroStaticCell,
                KakuroStaticCell
            ),
            listOf(
                KakuroClueCell(rightSum = 4, downSum = null),
                KakuroPlayableCell(solution = 1, userAnswer = null, hasError = false),
                KakuroPlayableCell(solution = 3, userAnswer = null, hasError = false),
                KakuroStaticCell,
                KakuroStaticCell
            ),
            listOf(
                KakuroClueCell(rightSum = 7, downSum = null),
                KakuroPlayableCell(solution = 3, userAnswer = null, hasError = false),
                KakuroPlayableCell(solution = 4, userAnswer = null, hasError = false),
                KakuroStaticCell,
                KakuroStaticCell
            ),
            listOf(
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell
            ),
            listOf(
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell,
                KakuroStaticCell
            )
        )
    }

}

