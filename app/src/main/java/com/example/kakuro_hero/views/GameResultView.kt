package com.example.kakuro_hero.views

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kakuro_hero.models.GameMode
import com.example.kakuro_hero.models.GameResult
import com.example.kakuro_hero.models.GameSession
import com.example.kakuro_hero.models.HintConstraints
import com.example.kakuro_hero.models.PressureType
import com.example.kakuro_hero.utils.EnumUtils
import com.example.kakuro_hero.utils.TimeUtils


class GameResultView {

    @Composable
    fun GameResultUI(
        gameSession: GameSession,
        onPlayAgainClick: () -> Unit,
        onHomeClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val isWin = gameSession.result == GameResult.WIN

        val title = if (isWin) "Puzzle Solved!"
        else "Puzzle Failed"

        val subtitle = if (isWin) {
            "Excellent work. You completed the Kakuro puzzle."
        } else {
            "You did not solve the puzzle this time. Take another shot."
        }

        val icon = if (isWin) Icons.Default.EmojiEvents else Icons.Default.Close

        val cardColor = if (isWin) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }

        val contentColor = if (isWin) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onErrorContainer
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                color = cardColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(72.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (gameSession.gameMode == GameMode.CHALLENGING) {
                        ScoreSection(
                            score = gameSession.score,
                            contentColor = contentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    SessionInfo(
                        session = gameSession,
                        contentColor = contentColor
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onPlayAgainClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Play Again")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onHomeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Back to Home")
                    }
                }
            }
        }
    }

    @Composable
    private fun ScoreSection(
        score: Int,
        contentColor: Color
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Score",
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
        }
    }

    @Composable
    private fun SessionInfo(
        session: GameSession,
        contentColor: Color
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Session Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )

                InfoRow("Mode", EnumUtils.toDisplayText(session.gameMode),
                    contentColor)
                InfoRow("Grid Size", session.gridSize.size.toString(), contentColor)
                InfoRow("Difficulty", EnumUtils.toDisplayText(session.difficulty),
                    contentColor)

                InfoRow("Time", TimeUtils.formatSeconds(session.sessionTime), contentColor)

                val displayMistakes = if (session.gameMode == GameMode.CHALLENGING &&
                    session.pressureType == PressureType.MISTAKE_LIMIT) {
                    "${session.mistakes} / ${session.maxMistakes}"
                }
                else {
                    "${session.mistakes}"
                }

                InfoRow(
                    "Mistakes",
                    displayMistakes,
                    contentColor
                )

                if (session.gameMode == GameMode.CHALLENGING) {

                    val hintsUsed =
                        (HintConstraints.getRule(session.difficulty).cellRevealCount - session.remainingCellHints) +
                                (HintConstraints.getRule(session.difficulty).rowRevealCount - session.remainingRowHints) +
                                (HintConstraints.getRule(session.difficulty).columnRevealCount - session.remainingColumnHints)


                    InfoRow(
                        "Hints Used",
                        hintsUsed.toString(),
                        contentColor
                    )
                }

            }
        }
    }

    @Composable
    private fun InfoRow(
        label: String,
        value: String,
        color: Color
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
