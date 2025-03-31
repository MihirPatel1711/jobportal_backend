package com.example.jobportal.job.models

data class RegisterRequest(val firstname: String, val lastname: String, val password: String, val email: String, val phoneNumber: String,  val role: String? = "USER" )
