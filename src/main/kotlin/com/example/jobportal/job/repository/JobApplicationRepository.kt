package com.example.jobportal.job.repository

import com.example.jobportal.job.models.JobApplication
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JobApplicationRepository(private val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        JobApplication(
            id = rs.getString("id"),
            jobId = rs.getString("job_id"),
            applicantId = rs.getString("applicant_id"),
            firstname = rs.getString("firstname"),
            lastname = rs.getString("lastname"),
            phoneNumber = rs.getString("phone_number"),
            resume = rs.getString("resume"),
            expectedSalary = rs.getDouble("expected_salary").takeIf { !rs.wasNull() },
            availabilityToJoin = rs.getString("availability_to_join"),
            portfolioLink = rs.getString("portfolio_link"),
            additionalComments = rs.getString("additional_comments"),
            status = rs.getString("status"),
            appliedAt = rs.getLong("applied_at")
        )
    }

    fun createJobApplication(application: JobApplication) {
        jdbcTemplate.update(
            """
            INSERT INTO job_applications 
            (id, job_id, applicant_id, firstname, lastname, phone_number, resume, expected_salary, availability_to_join, portfolio_link, additional_comments, status, applied_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            application.id, application.jobId, application.applicantId,
            application.firstname, application.lastname, application.phoneNumber,
            application.resume, application.expectedSalary, application.availabilityToJoin,
            application.portfolioLink, application.additionalComments,
            application.status, application.appliedAt
        )
    }

    fun getApplicationById(id: String): JobApplication? {
        return jdbcTemplate.query(
            "SELECT * FROM job_applications WHERE id = ?", rowMapper, id
        ).firstOrNull()
    }

    fun listApplicationsForJob(jobId: String): List<JobApplication> {
        return jdbcTemplate.query(
            "SELECT * FROM job_applications WHERE job_id = ? ORDER BY applied_at DESC", rowMapper, jobId
        )
    }

    fun updateApplicationStatus(id: String, status: String): Boolean {
        val updatedRows = jdbcTemplate.update(
            "UPDATE job_applications SET status = ? WHERE id = ?", status, id
        )
        return updatedRows > 0
    }

    fun deleteApplication(id: String): Boolean {
        return jdbcTemplate.update("DELETE FROM job_applications WHERE id = ?", id) > 0
    }

    fun listAllApplications(): List<JobApplication> {
        return jdbcTemplate.query(
            "SELECT * FROM job_applications ORDER BY applied_at DESC",
            rowMapper
        )
    }

    /**
     * 🔥 Fetch all applicants for jobs posted by a specific employer.
     * ✅ Uses a JOIN to match applications to jobs where `posted_by = employerId`
     */
    fun findApplicantsByEmployerId(employerId: String): List<JobApplication> {
        val sql = """
            SELECT ja.* FROM job_applications ja
            JOIN jobs j ON ja.job_id = j.id
            WHERE j.posted_by = ?   
            ORDER BY ja.applied_at DESC
        """
        return jdbcTemplate.query(sql, rowMapper, employerId)
    }
}
