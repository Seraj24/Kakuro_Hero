package com.example.kakuro_hero.views

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kakuro_hero.models.UserProfile
import com.example.kakuro_hero.services.ProfileValidator
import com.example.kakuro_hero.ui.FormContainer
import com.example.kakuro_hero.ui.FormHeader

class AccountView {

    @Composable
    fun AccountUI(
        user: UserProfile?,                  // null => guest/not signed in
        onBackClick: () -> Unit,
        onSaveClick: (String) -> Unit, // username, email
        onChangeProfileImageClick: () -> Unit,
        onSignInClick: () -> Unit,
        onSignUpClick: () -> Unit,
        onSignOutClick: () -> Unit,
        onDeleteClick: () -> Unit,
        modifier: Modifier,
        message: String? = null,              // controller can show success/error
        isLoading: Boolean = false
    ) {
        val corner = RoundedCornerShape(18.dp)

        // If is a guest
        if (user == null) {
            FormContainer(modifier = modifier, contentAlignment = Alignment.TopCenter) {
                FormHeader("Account")

                Spacer(Modifier.height(12.dp))

                Text(
                    "You are using Guest mode.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Sign In or Create an account to save progress and appear on the leaderboard.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = onSignInClick,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = corner
                ) { Text("Sign In") }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onSignUpClick,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = corner
                ) { Text("Sign Up") }

                Spacer(Modifier.height(18.dp))

                OutlinedButton(
                    onClick = onBackClick,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = corner
                ) { Text("Back") }
            }
            return
        }

        // Editable profile image
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        var profileImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        // Editable fields
        var username by remember(user) { mutableStateOf(user.username) }
        val email = user.email
        var isEditing by remember { mutableStateOf(false) }

        // Simple validation
        val usernameError = remember(username, isEditing) {
            ProfileValidator.validateUsername(username)
        }


        val canSave = isEditing && usernameError == null
                && (username.trim() != user.username)

        FormContainer(modifier = modifier, contentAlignment = Alignment.TopCenter) {

            FormHeader("Manage Account")

            Spacer(Modifier.height(14.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = corner,
                tonalElevation = 3.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(enabled = isEditing) {
                                onChangeProfileImageClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageBitmap != null) {
                            Image(
                                bitmap = profileImageBitmap!!,
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Add Profile Image",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        singleLine = true,
                        enabled = isEditing,
                        isError = usernameError != null,
                        supportingText = { if (usernameError != null) Text(usernameError) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {  },
                        label = { Text("Email") },
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    if (message != null) {
                        Text(
                            message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    if (!isEditing) {
                        Button(
                            onClick = { isEditing = true },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = corner
                        ) { Text("Edit Profile") }

                        Spacer(modifier = Modifier.height(12.dp))

                    } else {
                        Button(
                            onClick = { onSaveClick(username) },
                            enabled = canSave && !isLoading,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = corner
                        ) { Text("Save") }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                username = user.username
                                isEditing = false
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = corner
                        ) { Text("Cancel") }

                        Spacer(Modifier.height(12.dp))

                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = corner
            ) { Text("Back") }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onDeleteClick() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = corner,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete Account")
            }

            Spacer(Modifier.height(14.dp))

            OutlinedButton(
                onClick = onSignOutClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = corner
            ) {
                Text("Sign Out")
            }
        }
    }
}