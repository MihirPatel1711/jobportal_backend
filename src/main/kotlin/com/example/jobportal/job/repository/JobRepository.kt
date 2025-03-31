package com.example.jobportal.job.repository

import com.example.jobportal.job.models.Job
import com.example.jobportal.job.models.ListJobsRequest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JobRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        Job(
            id = rs.getString("id"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            company = rs.getString("company"),
            location = rs.getString("location"),
            salary = rs.getDouble("salary"),
            remote = rs.getBoolean("remote"),
            jobType = rs.getString("job_type"),
            requiredSkills = (rs.getArray("required_skills")?.array as? Array<String>)?.toList() ?: emptyList(),  // ✅ Fix Here
            educationLevel = rs.getString("education_level"),
            languageRequirement = rs.getString("language_requirement"),
            postedBy = rs.getString("posted_by"),
            createdAt = rs.getLong("created_at"),
            updatedAt = rs.getLong("updated_at")
        )
    }

        fun createJob(job: Job) {
            jdbcTemplate.update(
                "INSERT INTO jobs (id, title, description, company, location, salary, remote, job_type, required_skills, education_level, language_requirement, posted_by, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                job.id, job.title, job.description, job.company, job.location, job.salary, job.remote, job.jobType,
                job.requiredSkills.toTypedArray(), job.educationLevel, job.languageRequirement, job.postedBy, job.createdAt, job.updatedAt
            )
        }

    fun getJobById(id: String): Job? {
        return jdbcTemplate.query("SELECT * FROM jobs WHERE id = ?", rowMapper, id).firstOrNull()
    }

    fun updateJob(job: Job): Boolean {
        val updatedRows = jdbcTemplate.update(
            "UPDATE jobs SET title = ?, description = ?, company = ?, location = ?, salary = ?, remote = ?, job_type = ?, required_skills = ?, education_level = ?, language_requirement = ?, updated_at = ? WHERE id = ?",
            job.title, job.description, job.company, job.location, job.salary, job.remote, job.jobType,
            job.requiredSkills.toTypedArray(), job.educationLevel, job.languageRequirement, job.updatedAt, job.id
        )
        return updatedRows > 0
    }

    fun deleteJob(id: String): Boolean {
        return jdbcTemplate.update("DELETE FROM jobs WHERE id = ?", id) > 0
    }

    fun listJobs(request: ListJobsRequest): List<Job> {
        val params = mutableListOf<Any>()
        val sqlBuilder = StringBuilder("SELECT * FROM jobs WHERE 1=1")

        if (!request.search.isNullOrBlank()) {
            sqlBuilder.append(" AND (LOWER(title) LIKE ? OR LOWER(company) LIKE ? OR LOWER(description) LIKE ?)")
            val searchTerm = "%${request.search.lowercase()}%"
            params.addAll(listOf(searchTerm, searchTerm, searchTerm))
        }

        request.jobType?.let {
            sqlBuilder.append(" AND job_type = ?")
            params.add(it)
        }

        request.remote?.let {
            sqlBuilder.append(" AND remote = ?")
            params.add(it)
        }

        request.minSalary?.let {
            sqlBuilder.append(" AND salary >= ?")
            params.add(it)
        }

        request.maxSalary?.let {
            sqlBuilder.append(" AND salary <= ?")
            params.add(it)
        }

        request.location?.let {
            sqlBuilder.append(" AND location ILIKE ?")
            params.add("%$it%")
        }

        request.educationLevel?.let {
            sqlBuilder.append(" AND education_level = ?")
            params.add(it)
        }

        request.languageRequirement?.let {
            sqlBuilder.append(" AND language_requirement = ?")
            params.add(it)
        }

        // ✅ Fix: Filtering jobs based on requiredSkills
        request.requiredSkills?.takeIf { it.isNotEmpty() }?.let {
            sqlBuilder.append(" AND EXISTS (SELECT 1 FROM unnest(required_skills) AS skill WHERE skill = ANY(?))")
            params.add(it.toTypedArray()) // ✅ Convert List<String> to SQL Array
        }

        sqlBuilder.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?")
        val offset = (request.page!! - 1) * request.size!!
        params.add(request.size!!)
        params.add(offset)

        return jdbcTemplate.query(sqlBuilder.toString(), rowMapper, *params.toTypedArray())
    }

}

