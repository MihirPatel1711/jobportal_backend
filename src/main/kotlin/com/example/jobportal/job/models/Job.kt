package com.example.jobportal.job.models

data class Job(
    val id: String? = null,
    val title: String,
    val description: String,
    val company: String,
    val location: String,
    val salary: Double,
    val remote: Boolean,
    val jobType: String,  // Full-time, Part-time, Contract
    val requiredSkills: List<String>,
    val educationLevel: String?,
    val languageRequirement: String?,
    val postedBy: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class ListJobsRequest(
    val search: String?,
    val jobType: String?,
    val remote: Boolean?,
    val minSalary: Double?,
    val maxSalary: Double?,
    val location: String?,
    val requiredSkills: List<String>?,
    val educationLevel: String?,
    val languageRequirement: String?,
    val page: Int? = 1,
    val size: Int? = 10
)
