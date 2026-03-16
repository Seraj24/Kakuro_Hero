package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.UserProfile
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IAccountRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val DEFAULT_AVATAR_SEED = "vex"
    }

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Result<UserProfile> {
        return try {
            val trimmedUsername = username.trim()
            val trimmedEmail = email.trim()

            val authResult = auth.createUserWithEmailAndPassword(trimmedEmail, password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(IllegalStateException("Failed to create user."))

            val userProfile = UserProfile(
                id = firebaseUser.uid,
                username = trimmedUsername,
                email = trimmedEmail,
                avatarSeed = DEFAULT_AVATAR_SEED
            )

            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(userProfile)
                .await()

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<UserProfile> {
        return try {
            val trimmedEmail = email.trim()

            val authResult = auth.signInWithEmailAndPassword(trimmedEmail, password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(IllegalStateException("Failed to sign in user."))

            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userProfile = snapshot.toObject(UserProfile::class.java)
                ?: UserProfile(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: trimmedEmail,
                    avatarSeed = DEFAULT_AVATAR_SEED
                )

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<UserProfile?> {
        return try {
            val firebaseUser = auth.currentUser ?: return Result.success(null)

            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userProfile = snapshot.toObject(UserProfile::class.java)
                ?: UserProfile(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    avatarSeed = DEFAULT_AVATAR_SEED

                )

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(username: String): Result<UserProfile> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(IllegalStateException("No authenticated user found."))

            val currentEmail = firebaseUser.email ?: ""

            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val existingProfile = snapshot.toObject(UserProfile::class.java)

            val updatedProfile = UserProfile(
                id = firebaseUser.uid,
                username = username.trim(),
                email = currentEmail,
                avatarSeed = existingProfile?.avatarSeed ?: DEFAULT_AVATAR_SEED
            )

            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(updatedProfile)
                .await()

            Result.success(updatedProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(IllegalStateException("No authenticated user found."))

            val email = firebaseUser.email
                ?: return Result.failure(IllegalStateException("No email found for current user."))

            val credential = EmailAuthProvider.getCredential(email, currentPassword.trim())

            firebaseUser.reauthenticate(credential).await()
            firebaseUser.updatePassword(newPassword.trim()).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAvatarSeed(avatarSeed: String): Result<UserProfile> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(IllegalStateException("No authenticated user found."))

            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val existingProfile = snapshot.toObject(UserProfile::class.java)
                ?: UserProfile(
                    id = firebaseUser.uid,
                    username = "",
                    email = firebaseUser.email ?: "",
                    avatarSeed = "robot"
                )

            val updatedProfile = existingProfile.copy(avatarSeed = avatarSeed)

            firestore.collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(updatedProfile)
                .await()

            Result.success(updatedProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(IllegalStateException("No authenticated user found."))

            val uid = firebaseUser.uid

            // Delete profile data first
            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .delete()
                .await()

            // Then delete auth account
            firebaseUser.delete().await()

            Result.success(Unit)
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            Result.failure(
                IllegalStateException(
                    "For security reasons, please sign in again before deleting your account."
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }
}