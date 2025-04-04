package com.example.jobportal.job.repository


import com.example.jobportal.models.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

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
            role = rs.getString("role"),
            createdAt = rs.getLong("created_at")
        )
    }

    // ✅ Get User by ID
    fun findById(id: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", rowMapper, id).firstOrNull()
    }

    fun findByUsername(username: String): User? {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", rowMapper, username).firstOrNull()
    }

                fun listUsers(search: String?, page: Int, size: Int, getAll: Boolean): List<User> {
                    val params = mutableListOf<Any>()
                    val sqlBuilder = StringBuilder("SELECT * FROM users WHERE role IN ('USER', 'ADMIN')")

                    // ✅ Apply search filter only if search is provided
                    search?.trim()?.takeIf { it.isNotEmpty() }?.let {
                        sqlBuilder.append(" AND (LOWER(firstname) LIKE ? OR LOWER(lastname) LIKE ? OR LOWER(email) LIKE ?)")
                        val searchTerm = "%${it.lowercase()}%"
                        params.addAll(listOf(searchTerm, searchTerm, searchTerm))
                    }

                    // ✅ Apply pagination only if `getAll` is false
                    if (!getAll) {
                        sqlBuilder.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?")
                        val offset = (page - 1) * size
                        params.addAll(listOf(size, offset))
                    } else {
                        sqlBuilder.append(" ORDER BY created_at DESC") // Ensure sorting even when `getAll` is true
                    }

                    return jdbcTemplate.query(sqlBuilder.toString(), rowMapper, *params.toTypedArray())
                }


    fun updateUser(user: User): Boolean {
        val newUsername = "${user.firstname.lowercase()}.${user.lastname.lowercase()}"

        val updatedRows = jdbcTemplate.update(
            "UPDATE users SET firstname = ?, lastname = ?, email = ?, phone_number = ?, username = ? WHERE id = ?",
            user.firstname, user.lastname, user.email, user.phoneNumber, newUsername, user.id
        )
        return updatedRows > 0
    }

    // ✅ Delete User
    fun deleteUser(id: String): Boolean {
        jdbcTemplate.update("DELETE FROM refresh_tokens WHERE user_id = ?", id)
        val deletedRows = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id)
        return deletedRows > 0
    }
}