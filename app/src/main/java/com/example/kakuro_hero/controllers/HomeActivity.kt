package com.example.kakuro_hero.controllers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.kakuro_hero.models.UserProfile
import com.example.kakuro_hero.services.AccountRepository
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme
import com.example.kakuro_hero.views.HomeView
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private val accountRepository = AccountRepository()

    private var user by mutableStateOf<UserProfile?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loadCurrentUser()

        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView().HomeUI(
                        user = user,
                        onPlayClick = { navigateToGame() },
                        onSettingsClick = { navigateToSettings() },
                        onProfileClick = { navigateToAccount() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun loadCurrentUser() {
        lifecycleScope.launch {
            val result = accountRepository.getCurrentUser()

            result
                .onSuccess { loadedUser ->
                    user = loadedUser
                }
                .onFailure {
                    user = null
                }
        }
    }

    private fun navigateToSettings() {
        startActivity(
            Intent(
                this,
                SettingsActivity::class.java
            )
        )
    }

    private fun navigateToGame() {
        startActivity(
            Intent(
                this,
                GameSetupActivity::class.java
            )
        )
    }

    private fun navigateToLeaderboard() {
        // TODO
    }

    private fun navigateToAccount() {
        startActivity(
            Intent(
                this,
                AccountActivity::class.java
            )
        )

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Kakuro_Hero_First_PrototypeTheme {
        HomeView().HomeUI(
            user = UserProfile("1", "William", "william.henry.harrison@example-pet-store.com"),
            onPlayClick = { /*Nothing in the Preview*/ },
            onSettingsClick = { /*Nothing in the Preview*/ },
            onProfileClick = { /*Nothing in the Preview*/ },
            modifier = Modifier
        )
    }
}