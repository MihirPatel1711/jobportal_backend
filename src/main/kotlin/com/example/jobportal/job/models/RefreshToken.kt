package com.example.jobportal.job.models

data class RefreshToken(
    val id: String,
    val userId: String,
    val token: String,
    val expiresAt: Long,
    val createdAt: Long
)
