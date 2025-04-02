package com.example.jobportal.job.models

data class JobApplication(
    val id: String,
    val jobId: String,
    val applicantId: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val resume: String,
    val expectedSalary: Double?,
    val availabilityToJoin: String,
    val portfolioLink: String?,
    val additionalComments: String?,
    val status: String = "PENDING",
    val appliedAt: Long
)
