package com.example.jobportal.models

import java.util.*

data class User(
    val id: String,
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val userType: String, // 'admin', 'hr', or 'jobseeker'
    val companyName: String? = null,
    val companyWebsite: String? = null,
    val industry: String? = null,
    val companyLocation: String? = null,
    val createdAt: Long
)



data class ListUserInput(
    val search: String?,
    val page: Int?,
    val size: Int?,
    val getAll: Boolean?
)