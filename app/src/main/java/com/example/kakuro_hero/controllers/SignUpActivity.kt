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
import com.example.kakuro_hero.services.AccountRepository
import com.example.kakuro_hero.views.theme.Kakuro_Hero_First_PrototypeTheme
import com.example.kakuro_hero.views.SignUpView
import kotlinx.coroutines.launch

class SignUpActivity : ComponentActivity() {

    private val accountRepository = AccountRepository()

    private var message by mutableStateOf<String?>(null)
    private var isLoading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    SignUpView().SignUpUI(
                        onSignInClick = { navigateToSignIn() },
                        onSignUpClick = { username, email, password ->
                            createAccount(username, email, password)
                        },
                        message = null,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun createAccount(username: String, email: String, password: String) {
        lifecycleScope.launch {
            isLoading = true
            message = null

            val result = accountRepository.signUp(username, email, password)

            result
                .onSuccess {
                    message = "Account created successfully."
                    navigateToSignIn()
                }
                .onFailure { error ->
                    message = error.message ?: "Failed to create account."
                }

            isLoading = false
        }
    }

    fun navigateToSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Kakuro_Hero_First_PrototypeTheme() {
        SignUpView().SignUpUI(onSignInClick = { /*Nothing in the Preview*/ },
            onSignUpClick = { _, _, _ -> /*Nothing in the Preview*/ },
            message = null,
            modifier = Modifier)


    }
}