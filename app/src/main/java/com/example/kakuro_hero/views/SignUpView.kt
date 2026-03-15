package com.example.kakuro_hero.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.kakuro_hero.services.ProfileValidator
import com.example.kakuro_hero.ui.*


class SignUpView {

    @Composable
    public fun SignUpUI(
        onSignUpClick: (String, String, String) -> Unit,
        onSignInClick: () -> Unit,
        message: String? = null,
        isLoading: Boolean = false,
        modifier: Modifier) {

        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }



        val spacerHeight = 20.dp
        val boxPosition = Alignment.TopCenter

        // Check if the user focused/touched the field
        var usernameTouched by remember { mutableStateOf(false) }
        var emailTouched by remember { mutableStateOf(false) }
        var passwordTouched by remember { mutableStateOf(false) }

        // Simple validation
        val usernameError = remember(username) {
            ProfileValidator.validateUsername(username)
        }

        val emailError = remember(email) {
            ProfileValidator.validateEmail(email)
        }

        val passwordError = remember(password) {
            ProfileValidator.validatePassword(password)
        }

        val hasErrors = usernameError != null || emailError != null || passwordError != null


        FormContainer(modifier = modifier,
            contentAlignment = boxPosition) {

            FormHeader("Create an account to save your progress",
                textStyle = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(spacerHeight))


            SignUpTextField(label = "Username", value = username, onValueChange = { username = it },
                    error = usernameError, touched = usernameTouched,
                    onUnfocus = { usernameTouched = true })

            Spacer(modifier = Modifier.height(spacerHeight))

            SignUpTextField(label = "Email", value = email, onValueChange = { email = it },
                error = emailError, touched = emailTouched, onUnfocus = { emailTouched = true })

            Spacer(modifier = Modifier.height(spacerHeight))


            SignUpTextField(label = "Password", value = password, onValueChange = { password = it },
                error = passwordError, visualTransformation = PasswordVisualTransformation(),
                touched = passwordTouched, onUnfocus = { passwordTouched = true })

            if (message != null) {
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(spacerHeight))

            Button(onClick = {
                usernameTouched = true
                emailTouched = true
                passwordTouched = true

                if (!hasErrors) {
                    onSignUpClick(
                        username.trim(),
                        email.trim(),
                        password
                    )
                }
            },
                enabled = !isLoading, // Prevent multiple Sign Up attempts when Firestore is loading
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Sign Up", style = MaterialTheme.typography.headlineSmall)
            }

            TextButton(onClick = onSignInClick,
                enabled = !isLoading) // Prevent Sign In when Firestore is loading
            {
                Text(text = "Already have an account? Sign In",
                    color = MaterialTheme.colorScheme.primary)
            }

        }

    }

    @Composable
    private fun SignUpTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        error: String?,
        keyboardType: KeyboardType = KeyboardType.Text,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        touched: Boolean,
        onUnfocus: () -> Unit
    ) {

        // To make sure the user has focused on this field before showing an error, if any
        var hadFocus by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            isError = touched && error != null,
            supportingText = {
                if (touched && error != null) {
                    Text(error)
                }
            },
            modifier = Modifier.onFocusChanged { state ->
                if (state.isFocused) {
                    hadFocus = true
                } else if (hadFocus) {
                    onUnfocus()
                }
            }
        )
    }
}