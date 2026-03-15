package com.example.kakuro_hero.views

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.kakuro_hero.models.ChallengeConstraints
import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.GameMode
import com.example.kakuro_hero.models.GridSize
import com.example.kakuro_hero.models.PressureType
import com.example.kakuro_hero.utils.EnumUtils

class GameSetupView {

    @Composable
    fun GameSetupUI(
        selectedGameMode: GameMode,
        selectedDifficulty: Difficulty,
        selectedGridSize: GridSize,
        selectedPressureType: PressureType?,
        onGameModeSelected: (GameMode) -> Unit,
        onDifficultySelected: (Difficulty) -> Unit,
        onGridSizeSelected: (GridSize) -> Unit,
        onPressureTypeSelected: (PressureType) -> Unit,
        onStartGameClick: () -> Unit,
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SetupHeader(onBackClick = onBackClick)

            SetupSection(title = "Game Mode") {
                SingleChoiceChips(
                    options = GameMode.entries,
                    selectedOption = selectedGameMode,
                    optionLabel = { EnumUtils.toDisplayText(it) },
                    onOptionSelected = onGameModeSelected
                )
            }

            SetupSection(title = "Difficulty") {
                SingleChoiceChips(
                    options = Difficulty.entries,
                    selectedOption = selectedDifficulty,
                    optionLabel = { EnumUtils.toDisplayText(it) },
                    onOptionSelected = onDifficultySelected
                )
            }

            SetupSection(title = "Grid Size") {
                SingleChoiceChips(
                    options = GridSize.entries,
                    selectedOption = selectedGridSize,
                    optionLabel = { "${it.size} x ${it.size}" },
                    onOptionSelected = onGridSizeSelected
                )
            }

            if (selectedGameMode == GameMode.CHALLENGING) {
                SetupSection(title = "Pressure Type") {
                    SingleChoiceChips(
                        options = PressureType.entries,
                        selectedOption = selectedPressureType,
                        optionLabel = {
                            when (it) {
                                PressureType.TIME_BASED -> "Time Based"
                                PressureType.MISTAKE_LIMIT -> "Mistake Limit"
                                PressureType.KAKURO_HERO -> "Kakuro Hero"
                            }
                        },
                        onOptionSelected = onPressureTypeSelected
                    )
                }
            }

            ChallengeModeInfoBox(
                pressureType = selectedPressureType,
                modifier = modifier)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onStartGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @Composable
    private fun SetupHeader(
        onBackClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
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
                text = "New Game",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    private fun SetupSection(
        title: String,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                content()
            }
        }
    }

    @Composable
    private fun ChallengeModeInfoBox(
        pressureType: PressureType?,
        modifier: Modifier = Modifier
    ) {
        if (pressureType == null) return

        val title = when (pressureType) {
            PressureType.TIME_BASED -> "Time Challenge"
            PressureType.MISTAKE_LIMIT -> "Mistake Limit"
            PressureType.KAKURO_HERO -> "Kakuro Hero"
        }

        val challengeConstraints = ChallengeConstraints.getAllRulesForPressureType(pressureType)

        val description = buildString {

            when (pressureType) {
                PressureType.TIME_BASED -> {
                    append("Complete the puzzle before the countdown reaches zero.\n\n")

                    challengeConstraints.forEach { (difficulty, rule) ->
                        val minutes = rule.timeSeconds!! / 60
                        append(
                            "${
                                EnumUtils.toDisplayText(difficulty)}: " +
                                    "$minutes minutes\n"
                        )
                    }

                }
                PressureType.MISTAKE_LIMIT -> {
                    append("The game ends if you make more mistakes than allowed.\n\n")
                    challengeConstraints.forEach { (difficulty, rule) ->
                        val mistakes = rule.maxMistakes!!
                        append(
                            "${
                                EnumUtils.toDisplayText(difficulty)
                            }: " +
                                    "$mistakes mistakes\n"
                        )
                    }
                }
                PressureType.KAKURO_HERO -> {
                    append(
                        "Combines a countdown timer and a mistake limit with increased difficulty. " +
                                "Run out of time or make too many mistakes and the puzzle ends.\n\n"
                    )

                    challengeConstraints.forEach { (difficulty, rule) ->
                        val minutes = rule.timeSeconds!! / 60
                        val mistakes = rule.maxMistakes!!

                        append("${
                            EnumUtils.toDisplayText(difficulty)}: " +
                                "$minutes minutes, $mistakes mistakes\n")
                    }
                }
            }
        }


        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    private fun <T> SingleChoiceChips(
        options: List<T>,
        selectedOption: T?,
        optionLabel: (T) -> String,
        onOptionSelected: (T) -> Unit
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) },
                    label = {
                        Text(optionLabel(option))
                    }
                )
            }
        }
    }
}