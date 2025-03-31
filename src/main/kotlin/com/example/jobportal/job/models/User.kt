package com.example.jobportal.models

import java.util.*

data class User(
    val id: String? = null,
    val firstname: String,
    val lastname: String,
    val username: String, // Auto-generated from firstname & lastname
    val password: String,
    val email: String,
    val phoneNumber: String,
    val role: String, // "ADMIN" or "USER" stored as a string
    val createdAt: Long // Epoch time in milliseconds
)


data class ListUserInput(
    val search: String?,
    val page: Int?,
    val size: Int?,
    val getAll: Boolean?
)