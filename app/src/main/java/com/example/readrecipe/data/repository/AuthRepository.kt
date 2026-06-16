package com.example.readrecipe.data.repository

import com.example.readrecipe.data.local.dao.UserDao
import com.example.readrecipe.data.local.entity.UserEntity
import com.example.readrecipe.session.SessionManager
import java.security.MessageDigest

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun register(name: String, email: String, password: String): Result<Long> {
        return try {
            val existing = userDao.findByEmail(email)
            if (existing != null) {
                Result.failure(Exception("Email already registered"))
            } else {
                val user = UserEntity(
                    name = name,
                    email = email,
                    passwordHash = hashPassword(password)
                )
                val userId = userDao.insert(user)
                sessionManager.saveSession(userId)
                Result.success(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.findByEmail(email)
            when {
                user == null -> Result.failure(Exception("No account found with this email"))
                user.passwordHash != hashPassword(password) -> Result.failure(Exception("Incorrect password"))
                else -> {
                    sessionManager.saveSession(user.id)
                    Result.success(user)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    suspend fun getCurrentUser(): UserEntity? {
        val userId = sessionManager.getLoggedInUserId()
        if (userId == -1L) return null
        return userDao.findById(userId)
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
}
