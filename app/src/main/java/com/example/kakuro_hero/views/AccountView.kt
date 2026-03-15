package com.example.kakuro_hero.views


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kakuro_hero.models.UserProfile
import com.example.kakuro_hero.services.ProfileValidator
import com.example.kakuro_hero.ui.FormContainer
import com.example.kakuro_hero.ui.FormHeader
import com.example.kakuro_hero.utils.AvatarUtils

class AccountView {

    @Composable
    fun AccountUI(
        user: UserProfile?,
        onBackClick: () -> Unit,
        onSaveClick: (String) -> Unit,
        onAvatarSelectClick: (String) -> Unit,
        onSignInClick: () -> Unit,
        onSignUpClick: () -> Unit,
        onSignOutClick: () -> Unit,
        onDeleteClick: () -> Unit,
        modifier: Modifier,
        message: String? = null,
        isLoading: Boolean = false
    ) {
        val corner = RoundedCornerShape(20.dp)
        val scrollState = rememberScrollState()

        if (user == null) {
            FormContainer(
                modifier = modifier,
                contentAlignment = Alignment.TopCenter
            ) {

                FormHeader("Account")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You are using Guest mode.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in or create an account to save progress and appear on the leaderboard.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSignInClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = corner
                ) {
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSignUpClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = corner
                ) {
                    Text("Sign Up")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBackClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = corner
                ) {
                    Text("Back")
                }
            }
            return
        }

        var username by remember(user) { mutableStateOf(user.username) }
        var isEditing by remember(user) { mutableStateOf(false) }

        val usernameError = remember(username, isEditing) {
            ProfileValidator.validateUsername(username)
        }

        val canSave =
            isEditing &&
                    usernameError == null &&
                    username.trim() != user.username.trim()

        FormContainer(
            modifier = modifier,
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
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
                        text = "Manage Account",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                ProfileHero(user = user)

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = corner,
                    tonalElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "Choose Avatar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AvatarGrid(
                            selectedSeed = user.avatarSeed,
                            enabled = !isLoading,
                            onAvatarSelectClick = onAvatarSelectClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        singleLine = true,
                        enabled = isEditing && !isLoading,
                        isError = usernameError != null,
                        supportingText = {
                            if (usernameError != null) {
                                Text(usernameError)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        placeholder = {
                            Text("Enter your username")
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    if (message != null) {
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    if (!isEditing) {
                        Button(
                            onClick = { isEditing = true },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Edit Profile")
                        }
                    } else {
                        Button(
                            onClick = { onSaveClick(username.trim()) },
                            enabled = canSave && !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Save Changes")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                username = user.username
                                isEditing = false
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Account Actions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onSignOutClick,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "Sign Out",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onDeleteClick,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = "Delete Account",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

    }

    @Composable
    private fun ProfileHero(user: UserProfile) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = AvatarUtils.buildDiceBearUrl(user.avatarSeed),
                contentDescription = "Selected avatar",
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    @Composable
    private fun AvatarGrid(
        selectedSeed: String,
        enabled: Boolean,
        onAvatarSelectClick: (String) -> Unit
    ) {
        val seeds = AvatarUtils.availableSeeds.chunked(3)

        Column {
            seeds.forEachIndexed { rowIndex, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEachIndexed { itemIndex, seed ->
                        AvatarItem(
                            seed = seed,
                            isSelected = seed == selectedSeed,
                            enabled = enabled,
                            onClick = { onAvatarSelectClick(seed) },
                            modifier = Modifier.weight(1f)
                        )

                        if (itemIndex != rowItems.lastIndex) {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }

                    if (rowItems.size < 3) {
                        repeat(3 - rowItems.size) { emptyIndex ->
                            Spacer(modifier = Modifier.weight(1f))
                            if (emptyIndex != (3 - rowItems.size) - 1) {
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                }

                if (rowIndex != seeds.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    @Composable
    private fun AvatarItem(
        seed: String,
        isSelected: Boolean,
        enabled: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val borderColor =
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant

        Column(
            modifier = modifier.clickable(enabled = enabled, onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = AvatarUtils.buildDiceBearUrl(seed),
                    contentDescription = seed,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AvatarUtils.displayNameFromSeed(seed),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}