package com.example.kakuro_hero.services

object ProfileValidator {

    fun validateUsername(username: String): (String?) {
        val u = username.trim()
        if (u.length < 4) return "Username must be at least 4 characters."
        return null
    }

    fun validateEmail(email: String): String? {
        val e = email.trim()
        if (!e.contains("@") || !e.contains(".")) return "Enter a valid email."
        return null
    }

    fun validatePassword(password: String): String? {
        val p = password.trim()
        if (p.length < 6) return "Password must be at least 6 characters."
        return null

    }

}