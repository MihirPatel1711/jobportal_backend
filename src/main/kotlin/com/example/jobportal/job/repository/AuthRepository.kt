package com.example.jobportal.job.repository

import com.example.jobportal.job.models.RefreshToken
import com.example.jobportal.models.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class AuthRepository(private val jdbcTemplate: JdbcTemplate) {

    private val userRowMapper = RowMapper { rs, _ ->
        User(
            id = rs.getString("id"),
            firstname = rs.getString("firstname"),
            lastname = rs.getString("lastname"),
            username = rs.getString("username"),
            password = rs.getString("password"),
            email = rs.getString("email"),
            phoneNumber = rs.getString("phone_number"),
            role = rs.getString("role"),
            createdAt = rs.getLong("created_at")
        )
    }

    fun findByUsername(username: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", userRowMapper, username)
            .firstOrNull()
    }

    fun findById(id: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", userRowMapper, id)
            .firstOrNull()
    }

    fun save(user: User) {
        jdbcTemplate.update(
            "INSERT INTO users (id, firstname, lastname, username, password, email, phone_number, role, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            user.id, user.firstname, user.lastname, user.username, user.password,
            user.email, user.phoneNumber, user.role, user.createdAt
        )
    }

    fun saveRefreshToken(refreshToken: RefreshToken) {
        jdbcTemplate.update(
            "INSERT INTO refresh_tokens (id, user_id, token, expires_at, created_at) VALUES (?, ?, ?, ?, ?)",
            refreshToken.id, refreshToken.userId, refreshToken.token, refreshToken.expiresAt, refreshToken.createdAt
        )
    }

    fun findRefreshToken(refreshToken: String): RefreshToken? {
        val rowMapper = RowMapper { rs, _ ->
            RefreshToken(
                id = rs.getString("id"),
                userId = rs.getString("user_id"),
                token = rs.getString("token"),
                expiresAt = rs.getLong("expires_at"),
                createdAt = rs.getLong("created_at")
            )
        }
        return jdbcTemplate.query("SELECT * FROM refresh_tokens WHERE token = ?", rowMapper, refreshToken)
            .firstOrNull()
    }

    fun deleteRefreshToken(refreshToken: String): Boolean {
        return jdbcTemplate.update("DELETE FROM refresh_tokens WHERE token = ?", refreshToken) > 0
    }

    fun updateRefreshToken(userId: String, newToken: String, expiresAt: Long) {
        jdbcTemplate.update(
            "UPDATE refresh_tokens SET token = ?, expires_at = ? WHERE user_id = ?",
            newToken, expiresAt, userId
        )
    }

    fun deleteAllRefreshTokensForUser(userId: String): Boolean {
        return jdbcTemplate.update("DELETE FROM refresh_tokens WHERE user_id = ?", userId) > 0
    }
}
