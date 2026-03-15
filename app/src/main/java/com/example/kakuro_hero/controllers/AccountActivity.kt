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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme
import com.example.kakuro_hero.models.UserProfile
import com.example.kakuro_hero.services.AccountRepository
import com.example.kakuro_hero.views.AccountView
import kotlinx.coroutines.launch

class AccountActivity : ComponentActivity() {

    private val accountRepository = AccountRepository()

    private var user by mutableStateOf<UserProfile?>(null)
    private var message by mutableStateOf<String?>(null)
    private var isLoading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loadCurrentUser()

        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    //var user by remember { mutableStateOf<UserProfile?>(AccountRepository.getCurrentUser()) }
                    var message by remember { mutableStateOf<String?>(null) }

                    AccountView().AccountUI(
                        user = user,
                        onBackClick = { finish() },
                        onSaveClick = { newUsername ->
                            saveProfile(newUsername)
                        },
                        onAvatarSelectClick = { avatarSeed ->
                            updateAvatarSeed(avatarSeed)
                        },
                        onSignInClick = { navigateToSignIn() },
                        onSignUpClick = { navigateToSignUp() },
                        onSignOutClick = {
                            accountRepository.signOut()
                            user = null
                            finish()
                        },
                        onDeleteClick = { deleteAccount() },
                        modifier = Modifier.padding(innerPadding),
                        message = message,
                        isLoading = isLoading
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
                .onFailure { error ->
                    message = error.message ?: "Failed to load account."
                }
        }
    }

    private fun saveProfile(newUsername: String) {
        lifecycleScope.launch {
            val result = accountRepository.updateProfile(newUsername)

            result
                .onSuccess { updatedUser ->
                    user = updatedUser
                    message = "Profile updated successfully."
                }
                .onFailure { error ->
                    message = error.message ?: "Failed to update profile."
                }
        }
    }

    private fun updateAvatarSeed(avatarSeed: String) {
        lifecycleScope.launch {
            isLoading = true
            message = null

            val result = accountRepository.updateAvatarSeed(avatarSeed)

            result
                .onSuccess { updatedUser ->
                    user = updatedUser
                    message = "Avatar updated successfully."
                }
                .onFailure { error ->
                    message = error.message ?: "Failed to update avatar."
                }

            isLoading = false
        }
    }

    private fun deleteAccount() {
        lifecycleScope.launch {
            isLoading = true
            message = null

            val result = accountRepository.deleteAccount()

            result
                .onSuccess {
                    user = null
                    message = "Account deleted successfully."
                    navigateToSignIn()
                    finish()
                }
                .onFailure { error ->
                    message = error.message ?: "Failed to delete account."
                }

            isLoading = false
        }
    }

    private fun navigateToSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    Kakuro_Hero_First_PrototypeTheme {
        AccountView().AccountUI(
            user = UserProfile("1", "William", "william.henry.harrison@example-pet-store.com"),
            onBackClick = { /*Nothing in the Preview*/ },
            onSaveClick = { /*Nothing in the Preview*/ },
            onAvatarSelectClick = { /*Nothing in the Preview*/ },
            onSignInClick = { /*Nothing in the Preview*/ },
            onSignUpClick = { /*Nothing in the Preview*/ },
            onSignOutClick = { /*Nothing in the Preview*/ },
            onDeleteClick = { /*Nothing in the Preview*/ },
            modifier = Modifier
        )
    }
}