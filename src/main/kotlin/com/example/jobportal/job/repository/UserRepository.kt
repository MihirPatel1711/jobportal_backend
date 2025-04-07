package com.example.jobportal.job.repository

import com.example.jobportal.models.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        User(
            id = rs.getString("id"),
            firstname = rs.getString("firstname"),
            lastname = rs.getString("lastname"),
            username = rs.getString("username"),
            password = rs.getString("password"),
            email = rs.getString("email"),
            phoneNumber = rs.getString("phone_number"),
            userType = rs.getString("user_type"),
            companyName = rs.getString("company_name"),
            companyWebsite = rs.getString("company_website"),
            industry = rs.getString("industry"),
            companyLocation = rs.getString("company_location"),
            createdAt = rs.getLong("created_at")
        )
    }

    fun findById(id: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", rowMapper, id).firstOrNull()
    }

    fun findByUsername(username: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", rowMapper, username).firstOrNull()
    }

    fun listUsers(search: String?, page: Int, size: Int, getAll: Boolean): List<User> {
        val params = mutableListOf<Any>()
        val sqlBuilder = StringBuilder("SELECT * FROM users WHERE 1=1")

        search?.trim()?.takeIf { it.isNotEmpty() }?.let {
            sqlBuilder.append(" AND (LOWER(firstname) LIKE ? OR LOWER(lastname) LIKE ? OR LOWER(email) LIKE ?)")
            val searchTerm = "%${it.lowercase()}%"
            params.addAll(listOf(searchTerm, searchTerm, searchTerm))
        }

        if (!getAll) {
            sqlBuilder.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?")
            val offset = (page - 1) * size
            params.addAll(listOf(size, offset))
        } else {
            sqlBuilder.append(" ORDER BY created_at DESC")
        }

        return jdbcTemplate.query(sqlBuilder.toString(), rowMapper, *params.toTypedArray())
    }

    fun updateUser(user: User): Boolean {
        val newUsername = "${user.firstname.lowercase()}.${user.lastname.lowercase()}"

        val updatedRows = jdbcTemplate.update(
            """
            UPDATE users SET 
                firstname = ?, 
                lastname = ?, 
                email = ?, 
                phone_number = ?, 
                username = ?, 
                user_type = ?, 
                company_name = ?, 
                company_website = ?, 
                industry = ?, 
                company_location = ? 
            WHERE id = ?
            """.trimIndent(),
            user.firstname,
            user.lastname,
            user.email,
            user.phoneNumber,
            newUsername,
            user.userType,
            user.companyName,
            user.companyWebsite,
            user.industry,
            user.companyLocation,
            user.id
        )
        return updatedRows > 0
    }

    fun deleteUser(id: String): Boolean {
        jdbcTemplate.update("DELETE FROM refresh_tokens WHERE user_id = ?", id)
        val deletedRows = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id)
        return deletedRows > 0
    }
}
