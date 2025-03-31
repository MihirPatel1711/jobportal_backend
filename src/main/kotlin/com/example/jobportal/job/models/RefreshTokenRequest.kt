package com.example.jobportal.job.models

data class RefreshTokenRequest(
    val refreshToken: String  // ✅ Removed `userId`, since it's not needed
)
