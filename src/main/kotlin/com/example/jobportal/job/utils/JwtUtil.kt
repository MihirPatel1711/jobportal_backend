package com.example.jobportal.job.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.io.Decoders
import java.util.*
import javax.crypto.SecretKey
import org.springframework.stereotype.Component

@Component
class JwtUtil {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("yourSecretKeyReplaceThisWithAtLeast32Characters"))
    private val refreshSecretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("yourRefreshSecretKeyReplaceThisWithAtLeast32Characters"))

    fun generateAccessToken(username: String, role: String): String {
        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes expiry
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days expiry
            .signWith(refreshSecretKey)
            .compact()
    }

    fun validateToken(token: String, isRefresh: Boolean = false): Boolean {
        return try {
            val key = if (isRefresh) refreshSecretKey else secretKey
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractUsername(token: String, isRefresh: Boolean = false): String {
        return getClaims(token, isRefresh).subject
    }

    private fun getClaims(token: String, isRefresh: Boolean) =
        Jwts.parser().verifyWith(if (isRefresh) refreshSecretKey else secretKey).build().parseSignedClaims(token).payload
}