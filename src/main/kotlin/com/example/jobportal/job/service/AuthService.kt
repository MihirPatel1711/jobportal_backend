package com.example.jobportal.job.service

import com.example.jobportal.job.models.AuthResponse
import com.example.jobportal.job.models.RefreshToken
import com.example.jobportal.job.models.RefreshTokenRequest
import com.example.jobportal.job.repository.AuthRepository
import com.example.jobportal.job.utils.JwtUtil
import com.example.jobportal.models.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun registerUser(firstname: String, lastname: String, password: String, email: String, phoneNumber: String, role: String): User {
        val id = UUID.randomUUID().toString()
        val username = generateUsername(firstname, lastname)
        val hashedPassword = passwordEncoder.encode(password)
        val createdAt = System.currentTimeMillis()

        val user = User(
            id = id,
            firstname = firstname,
            lastname = lastname,
            username = username,
            password = hashedPassword,
            email = email,
            phoneNumber = phoneNumber,
            role = role,
            createdAt = createdAt
        )
        authRepository.save(user)
        return user
    }

    fun authenticate(username: String, password: String): AuthResponse? {
        val user = authRepository.findByUsername(username) ?: return null
        if (!passwordEncoder.matches(password, user.password)) return null

        val accessToken = jwtUtil.generateAccessToken(user.username, user.role)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)

        val refreshTokenId = UUID.randomUUID().toString()
        val expiresAt = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7) // 7 days expiry

        authRepository.saveRefreshToken(RefreshToken(refreshTokenId, user.id.toString(), refreshToken, expiresAt, System.currentTimeMillis()))

        return AuthResponse(accessToken, refreshToken)
    }


    fun refreshAccessToken(request: RefreshTokenRequest): String? {
        if (request.refreshToken.isBlank()) return null  // ✅ Ensure `refreshToken` is provided

        val storedToken = authRepository.findRefreshToken(request.refreshToken) ?: return null
        if (storedToken.expiresAt < System.currentTimeMillis()) return null // ✅ Token expired

        val user = authRepository.findById(storedToken.userId) ?: return null

        return jwtUtil.generateAccessToken(user.username, user.role)
    }



    fun revokeRefreshToken(refreshToken: String): Boolean {
        return authRepository.deleteRefreshToken(refreshToken)
    }

    fun revokeAllTokensForUser(userId: String): Boolean {
        return authRepository.deleteAllRefreshTokensForUser(userId)
    }

    private fun generateUsername(firstname: String, lastname: String): String {
        val baseUsername = "${firstname.lowercase()}.${lastname.lowercase()}"
        var finalUsername = baseUsername
        var counter = 1

        while (authRepository.findByUsername(finalUsername) != null) {
            finalUsername = "$baseUsername$counter"
            counter++
        }
        return finalUsername
    }
}
