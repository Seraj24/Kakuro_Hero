package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.UserProfile

interface IAccountRepository {
    suspend fun signUp(username: String, email: String, password: String): Result<UserProfile>
    suspend fun signIn(email: String, password: String): Result<UserProfile>
    suspend fun getCurrentUser(): Result<UserProfile?>
    suspend fun updateProfile(username: String): Result<UserProfile>
    suspend fun deleteAccount(): Result<Unit>
    fun signOut()
    fun isUserSignedIn(): Boolean
}