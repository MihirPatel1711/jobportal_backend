package com.example.jobportal.job.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Configuration
@Component
class JwtUtil {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))
    private val refreshSecretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(REFRESH_SECRET_KEY))

    companion object {
        private const val SECRET_KEY = "yourSecretKeyReplaceThisWithAtLeast32Characters"
        private const val REFRESH_SECRET_KEY = "yourRefreshSecretKeyReplaceThisWithAtLeast32Characters"
    }

    // Generate Access Token
    fun generateAccessToken(username: String, userId: String, userType: String): String {
        return Jwts.builder()
            .subject(username)
            .claim("user_id", userId)
            .claim("user_type", userType)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min expiry
            .signWith(secretKey)
            .compact()
    }

    // Generate Refresh Token
    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days expiry
            .signWith(refreshSecretKey)
            .compact()
    }

    // Validate Token
    fun validateToken(token: String, isRefresh: Boolean = false): Boolean {
        return try {
            val key = if (isRefresh) refreshSecretKey else secretKey
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Extract Username from Token
    fun extractUsername(token: String, isRefresh: Boolean = false): String {
        return getClaims(token, isRefresh).subject
    }

    // Extract Claims
    private fun getClaims(token: String, isRefresh: Boolean): Claims {
        val key = if (isRefresh) refreshSecretKey else secretKey
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    }

    fun extractUserId(token: String): String? {
        return try {
            getClaims(token, false)["user_id"] as? String
        } catch (e: Exception) {
            null
        }
    }

    fun extractUserType(token: String): String? {
        return try {
            getClaims(token, false)["user_type"] as? String
        } catch (e: Exception) {
            null
        }
    }
}
