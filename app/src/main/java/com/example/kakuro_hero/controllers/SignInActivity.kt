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
import com.example.kakuro_hero.views.SignInView
import kotlinx.coroutines.launch

class SignInActivity : ComponentActivity() {

    private val accountRepository = AccountRepository()

    private var message by mutableStateOf<String?>(null)
    private var isLoading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kakuro_Hero_First_PrototypeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var message by remember { mutableStateOf<String?>(null) }

                    SignInView().SignInUI(
                        onSignInClick = { email, password ->
                            logIn(email, password)
                        },
                        onSignUpClick = { navigateToSignUp() },
                        onGuestClick = { guestSignIn() },
                        message = message,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun logIn(email: String, password: String) {
        lifecycleScope.launch {
            isLoading = true
            message = null

            val result = accountRepository.signIn(email, password)

            result
                .onSuccess {
                    navigateToHome()
                }
                .onFailure { error ->
                    message = error.message ?: "Invalid email or password."
                }

            isLoading = false
        }
    }
    private fun navigateToSignUp() {
        startActivity(Intent(this,
            SignUpActivity::class.java))
    }

    private fun navigateToHome() {
        startActivity(Intent(this,
            HomeActivity::class.java))
    }

    private fun guestSignIn() {
        navigateToHome()
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kakuro_Hero_First_PrototypeTheme {
        SignInView().SignInUI(onSignInClick = { _, _ -> },
            onSignUpClick = { /*Nothing in the Preview*/ },
            onGuestClick = { /*Nothing in the Preview*/ },
            modifier = Modifier)
    }
}