package com.example.kakuro_hero.views


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakuro_hero.models.UserProfile

class HomeView {

    @Composable
    fun HomeUI(
        user: UserProfile?,
        onPlayClick: () -> Unit,
        onSettingsClick: () -> Unit,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp, bottom = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "KAKURO",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "HERO",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 8.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(64.dp))

                KakuroBrandMark()

                Spacer(modifier = Modifier.height(52.dp))

                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "PLAY",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun KakuroBrandMark() {

        val black = Color.Black
        val white = MaterialTheme.colorScheme.surface
        val accent = MaterialTheme.colorScheme.primary
        val border = MaterialTheme.colorScheme.outline

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTile(black)
                BrandTile(accent)
                BrandTile(accent)
                BrandTile(black)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTile(black)
                BrandTile(white, border)
                BrandTile(white, border)
                BrandTile(accent)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTile(black)
                BrandTile(white, border)
                BrandTile(white, border)
                BrandTile(white, border)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BrandTile(black)
                BrandTile(accent)
                BrandTile(white, border)
                BrandTile(white, border)
            }
        }
    }

    @Composable
    private fun BrandTile(
        backgroundColor: Color,
        borderColor: Color? = null
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(backgroundColor)
                .then(
                    if (borderColor != null) {
                        Modifier.border(
                            1.dp,
                            borderColor,
                            RoundedCornerShape(6.dp)
                        )
                    } else Modifier
                )
        )
    }
}


