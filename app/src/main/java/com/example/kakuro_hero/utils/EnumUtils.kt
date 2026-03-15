package com.example.kakuro_hero.utils

object EnumUtils {

    fun toDisplayText(enum: Enum<*>): String {
        return enum.name.lowercase()
            .replaceFirstChar { it.uppercase() }
    }

}