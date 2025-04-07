package com.example.jobportal.job.models

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val userType: String,
    val companyName: String? = null,
    val companyWebsite: String? = null,
    val industry: String? = null,
    val companyLocation: String? = null
)
