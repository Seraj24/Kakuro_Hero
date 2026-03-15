package com.example.kakuro_hero.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakuro_hero.ui.*



class SignInView {


    @Composable
    fun SignInUI(
        onSignInClick: (String, String) -> Unit,
        onSignUpClick: () -> Unit,
        onGuestClick: () -> Unit,
        isLoading: Boolean = false,
        message: String? = null,
        modifier: Modifier) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val spacerHeight = 20.dp
        val boxPosition = Alignment.TopCenter
        val cornerShape = RoundedCornerShape(12.dp)

        FormContainer(modifier = modifier,
            contentAlignment = boxPosition) {

                FormHeader("Sign In")

                Spacer(modifier = Modifier.height(spacerHeight))

                OutlinedTextField(value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = cornerShape,
                    modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(spacerHeight))

                OutlinedTextField(value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = cornerShape,
                    modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(spacerHeight))

                if (message != null) {
                    Text(
                    message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(spacerHeight))


                Button(onClick = { onSignInClick(email.trim(), password) },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = cornerShape) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Sign In", style = MaterialTheme.typography.headlineSmall)
                    }

                }

                TextButton(onClick = onSignUpClick,
                    enabled = !isLoading) {
                    Text(text = "Don't have an account? Click to Sign Up",
                        color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(spacerHeight))

                OutlinedButton(
                    onClick = onGuestClick,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = cornerShape
                ) {
                    Text("Continue as a guest")
                }
                Spacer(modifier = Modifier.height(6.dp))

                Text("Note: Guest accounts cannot save progress or " +
                        "appear on the leaderboard",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }




    }

}