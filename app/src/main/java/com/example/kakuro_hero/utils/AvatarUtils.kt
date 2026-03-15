package com.example.kakuro_hero.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object AvatarUtils {

    val availableSeeds = listOf(
        "atlas",
        "nova",
        "orion",
        "pixel",
        "bolt",
        "echo"
    )

    fun displayNameFromSeed(seed: String): String {
        return when (seed) {
            "atlas" -> "Atlas"
            "nova" -> "Nova"
            "orion" -> "Orion"
            "pixel" -> "Pixel"
            "bolt" -> "Bolt"
            "echo" -> "Echo"
            else -> "Unit"
        }
    }

    fun buildDiceBearUrl(seed: String): String {
        val encodedSeed = URLEncoder.encode(seed, StandardCharsets.UTF_8.toString())
        return "https://api.dicebear.com/9.x/bottts/svg?seed=$encodedSeed"
    }
}