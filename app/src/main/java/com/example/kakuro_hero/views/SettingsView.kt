package com.example.kakuro_hero.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kakuro_hero.ui.FormContainer
import com.example.kakuro_hero.ui.FormHeader

class SettingsView {

    @Composable
    fun SettingsUI(
        onBackClick: () -> Unit,
        onManageAccountClick: () -> Unit,
        modifier: Modifier
    ) {
        val sfxEnabled = remember { mutableStateOf(true) }
        val hintsEnabled = remember { mutableStateOf(true) }

        FormContainer(
            modifier = modifier,
            contentAlignment = Alignment.TopCenter
        ) {
            FormHeader("Settings")

            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                title = "Sound Effects",
                checked = sfxEnabled.value,
                onCheckedChange = { sfxEnabled.value = it }
            )

            SettingRow(
                title = "Hints",
                checked = hintsEnabled.value,
                onCheckedChange = { hintsEnabled.value = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onManageAccountClick() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Account"
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text("Manage your Account")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "About",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Kakuro Hero\nMade with Kotlin + Jetpack Compose",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Back to Home")
            }
        }
    }

    @Composable
    private fun SettingRow(
        title: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}