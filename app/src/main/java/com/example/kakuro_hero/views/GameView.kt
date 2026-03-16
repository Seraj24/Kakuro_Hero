package com.example.kakuro_hero.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kakuro_hero.models.CellPosition
import com.example.kakuro_hero.models.KakuroCell
import com.example.kakuro_hero.models.KakuroClueCell
import com.example.kakuro_hero.models.KakuroPlayableCell
import com.example.kakuro_hero.models.KakuroStaticCell
import com.example.kakuro_hero.models.PressureType
import com.example.kakuro_hero.models.Puzzle
import com.example.kakuro_hero.utils.TimeUtils

class GameView {

    @Composable
    fun GameUI(
        puzzle: Puzzle,
        selectedCell: CellPosition?,
        onCellClick: (CellPosition) -> Unit,
        onNumberClick: (Int) -> Unit,
        onClearClick: () -> Unit,
        onClearAllClick: () -> Unit,
        onCheckClick: () -> Unit,
        onBackClick: () -> Unit,
        onPauseClick: () -> Unit,
        onFinishClick: () -> Unit,
        onViewResultsClick: () -> Unit,
        onCellHintClick: () -> Unit,
        onRowHintClick: () -> Unit,
        onColumnHintClick: () -> Unit,
        remainingCellHints: Int,
        remainingRowHints: Int,
        remainingColumnHints: Int,
        hintedCells: Set<CellPosition>,
        showMistakes: Boolean = false,
        gameEnded: Boolean = false,
        isPaused: Boolean,
        resultReady: Boolean,
        countdownSeconds: Int,
        isChallengingMode: Boolean,
        challengeType: PressureType?,
        mistakeCount: Int?,
        maxMistakes: Int?,
        remainingSeconds: Int,
        modifier: Modifier = Modifier,
        message: String? = null
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            GameHeader(onBackClick = onBackClick, onPauseClick = onPauseClick, isPaused = isPaused,
                showPauseButton = !gameEnded)

            Spacer(modifier = Modifier.height(16.dp))

            if (isChallengingMode) {
                ChallengeStatusBar(
                    challengeType = challengeType,
                    remainingSeconds = remainingSeconds,
                    mistakeCount = mistakeCount,
                    maxMistakes = maxMistakes
                )

                Spacer(modifier = Modifier.height(16.dp))

                HintSection(
                    remainingCellHints = remainingCellHints,
                    remainingRowHints = remainingRowHints,
                    remainingColumnHints = remainingColumnHints,
                    onCellHintClick = onCellHintClick,
                    onRowHintClick = onRowHintClick,
                    onColumnHintClick = onColumnHintClick,
                    gameEnded = gameEnded
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PuzzleGrid(
                        puzzle = puzzle,
                        selectedCell = selectedCell,
                        showMistakes = showMistakes,
                        hintedCells = hintedCells,
                        onCellClick = onCellClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            NumberPad(
                onNumberClick = onNumberClick,
                gameEnded
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ClearButton(
                        onClearClick = onClearClick,
                        gameEnded = gameEnded,
                        modifier = Modifier.weight(1f)
                    )

                    ClearAllButton(
                        onClearAllClick = onClearAllClick,
                        gameEnded = gameEnded,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (!isChallengingMode) {
                    CheckButton(
                        onCheckClick = onCheckClick,
                        showMistakes = showMistakes,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            if (gameEnded) {
                Spacer(modifier = Modifier.height(20.dp))

                if (!resultReady) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Preparing results... $countdownSeconds")
                    }
                }

                AnimatedVisibility(
                    visible = resultReady,
                    enter = fadeIn(
                        animationSpec = tween(400)
                    ) + scaleIn(
                        initialScale = 0.85f,
                        animationSpec = tween(400)
                    )
                ) {
                    AnimatedResultsButton(
                        onClick = onViewResultsClick
                    )
                }
            } else {
                FinishButton(
                    onFinishClick = onFinishClick,
                    gameEnded = gameEnded,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }

    @Composable
    private fun GameHeader(
        onBackClick: () -> Unit,
        onPauseClick: () -> Unit,
        isPaused: Boolean,
        showPauseButton: Boolean
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = if (isPaused) "Kakuro Hero - Paused" else "Kakuro Hero",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.Center)
                )

                if (showPauseButton) {
                    IconButton(
                        onClick = onPauseClick,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Resume" else "Pause"
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AnimatedResultsButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "results_button_pulse")

        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(490),
                repeatMode = RepeatMode.Reverse
            ),
            label = "results_button_scale"
        )

        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(scale),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("View Results")
        }
    }

    @Composable
    private fun ChallengeStatusBar(
        challengeType: PressureType?,
        remainingSeconds: Int?,
        mistakeCount: Int?,
        maxMistakes: Int?
    ) {

        if (challengeType == null) return

        val showTime =
            challengeType == PressureType.TIME_BASED ||
                    challengeType == PressureType.KAKURO_HERO

        val showMistakes =
            challengeType == PressureType.MISTAKE_LIMIT ||
                    challengeType == PressureType.KAKURO_HERO

        val visibleCount = listOf(showTime, showMistakes).count { it }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showTime) {

                val modifier =
                    if (visibleCount == 1)
                        Modifier.fillMaxWidth(0.6f)
                    else
                        Modifier.weight(1f)

                ChallengeCard(
                    title = "Time",
                    value = TimeUtils.formatSeconds(remainingSeconds ?: 0),
                    modifier = modifier
                )
            }

            if (showMistakes) {

                if (visibleCount == 2) {
                    Spacer(modifier = Modifier.width(12.dp))
                }

                val currentMistakes = mistakeCount ?: 0
                val allowedMistakes = maxMistakes ?: 0

                val modifier =
                    if (visibleCount == 1)
                        Modifier.fillMaxWidth(0.6f)
                    else
                        Modifier.weight(1f)

                ChallengeCard(
                    title = "Mistakes",
                    value = "$currentMistakes / $allowedMistakes",
                    valueColor =
                        if (allowedMistakes > 0 && currentMistakes >= allowedMistakes)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface,
                    modifier = modifier
                )
            }
        }
    }

    @Composable
    private fun ChallengeCard(
        title: String,
        value: String,
        modifier: Modifier = Modifier,
        valueColor: Color = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }

    @Composable
    private fun HintSection(
        remainingCellHints: Int,
        remainingRowHints: Int,
        remainingColumnHints: Int,
        onCellHintClick: () -> Unit,
        onRowHintClick: () -> Unit,
        onColumnHintClick: () -> Unit,
        gameEnded: Boolean
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Hints",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Hints",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HintButton(
                        title = "Cell",
                        count = remainingCellHints,
                        icon = Icons.Default.GridView,
                        onClick = onCellHintClick,
                        enabled = !gameEnded && remainingCellHints > 0,
                        modifier = Modifier.weight(1f)
                    )

                    HintButton(
                        title = "Row",
                        count = remainingRowHints,
                        icon = Icons.Default.TableRows,
                        onClick = onRowHintClick,
                        enabled = !gameEnded && remainingRowHints > 0,
                        modifier = Modifier.weight(1f)
                    )

                    HintButton(
                        title = "Column",
                        count = remainingColumnHints,
                        icon = Icons.Default.ViewColumn,
                        onClick = onColumnHintClick,
                        enabled = !gameEnded && remainingColumnHints > 0,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    private fun HintButton(
        title: String,
        count: Int,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        onClick: () -> Unit,
        enabled: Boolean,
        modifier: Modifier = Modifier
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "${title}_hint_animation")

        val iconScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (enabled) 1.08f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "${title}_hint_scale"
        )

        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.height(78.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(20.dp)
                        .scale(iconScale)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }

    @Composable
    private fun PuzzleGrid(
        puzzle: Puzzle,
        selectedCell: CellPosition?,
        showMistakes: Boolean,
        hintedCells: Set<CellPosition>,
        onCellClick: (CellPosition) -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            puzzle.grid.cells.forEachIndexed { rowIndex, row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    row.forEachIndexed { columnIndex, cell ->
                        val position = CellPosition(rowIndex, columnIndex)

                        KakuroBoardCell(
                            cell = cell,
                            isSelected = selectedCell == position,
                            showMistakes = showMistakes,
                            isHintHighlighted = hintedCells.contains(position),
                            onClick = {
                                if (cell is KakuroPlayableCell) {
                                    onCellClick(position)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun KakuroBoardCell(
        cell: KakuroCell,
        isSelected: Boolean,
        showMistakes: Boolean,
        isHintHighlighted: Boolean,
        onClick: () -> Unit
    ) {
        val borderColor = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }

        when (cell) {
            is KakuroStaticCell -> {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Black)
                        .border(1.dp, borderColor, MaterialTheme.shapes.small)
                )
            }

            is KakuroClueCell -> {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.DarkGray)
                        .border(1.dp, borderColor, MaterialTheme.shapes.small)
                        .padding(4.dp)
                ) {
                    if (cell.downSum != null) {
                        Text(
                            text = cell.downSum.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }

                    if (cell.rightSum != null) {
                        Text(
                            text = cell.rightSum.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
            }

            is KakuroPlayableCell -> {
                val targetBackgroundColor = when {
                    showMistakes && cell.hasError -> MaterialTheme.colorScheme.errorContainer
                    isHintHighlighted -> Color(0xFFFFC107)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }

                val animatedBackgroundColor by animateColorAsState(
                    targetValue = targetBackgroundColor,
                    label = "cellBackgroundColor"
                )

                val targetBorderColor = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isHintHighlighted -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.outline
                }

                val animatedBorderColor by animateColorAsState(
                    targetValue = targetBorderColor,
                    label = "cellBorderColor"
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(animatedBackgroundColor)
                        .border(2.dp, animatedBorderColor, MaterialTheme.shapes.small)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cell.userAnswer?.toString() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            showMistakes && cell.hasError ->
                                MaterialTheme.colorScheme.onErrorContainer

                            isHintHighlighted ->
                                Color.Black

                            showMistakes && cell.userAnswer != null ->
                                Color.Green

                            else ->
                                MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun NumberPad(
        onNumberClick: (Int) -> Unit,
        gameEnded: Boolean,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                (1..5).forEach { number ->
                    Button(
                        onClick = { onNumberClick(number) },
                        enabled = !gameEnded,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(number.toString())
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                (6..9).forEach { number ->
                    Button(
                        onClick = { onNumberClick(number) },
                        enabled = !gameEnded,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(number.toString())
                    }
                }

            }
        }
    }

    @Composable
    private fun ClearButton(
        onClearClick: () -> Unit,
        gameEnded: Boolean,
        modifier: Modifier = Modifier
    ) {
        OutlinedButton(
            onClick = onClearClick,
            enabled = !gameEnded,
            modifier = modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(
                text = "Clear",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    private fun ClearAllButton(
        onClearAllClick: () -> Unit,
        gameEnded: Boolean,
        modifier: Modifier = Modifier
    ) {
        OutlinedButton(
            onClick = onClearAllClick,
            enabled = !gameEnded,
            modifier = modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(
                text = "Clear All",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    private fun CheckButton(
        onCheckClick: () -> Unit,
        showMistakes: Boolean,
        modifier: Modifier = Modifier
    ) {
        Button(
            onClick = onCheckClick,
            modifier = modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (showMistakes) "Hide Mistakes" else "Show Mistakes",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    private fun FinishButton(
        onFinishClick: () -> Unit,
        gameEnded: Boolean,
        modifier: Modifier = Modifier
    ) {
        Button(
            onClick = onFinishClick,
            enabled = !gameEnded,
            modifier = modifier
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (gameEnded) "Puzzle Finished" else "Finish Puzzle",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}