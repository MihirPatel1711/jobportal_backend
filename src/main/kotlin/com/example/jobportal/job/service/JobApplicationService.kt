package com.example.jobportal.job.service

import com.example.jobportal.job.models.JobApplication
import com.example.jobportal.job.repository.JobApplicationRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class JobApplicationService(private val jobApplicationRepository: JobApplicationRepository) {

        fun applyForJob(
            jobId: String,
            applicantId: String,
            firstname: String,
            lastname: String,
            phoneNumber: String,
            resume: String,
            expectedSalary: Double?,
            availabilityToJoin: String,
            portfolioLink: String?,
            additionalComments: String?
        ): JobApplication {
            val id = UUID.randomUUID().toString()
            val appliedAt = System.currentTimeMillis()

            val application = JobApplication(
                id = id,
                jobId = jobId,
                applicantId = applicantId,
                firstname = firstname,
                lastname = lastname,
                phoneNumber = phoneNumber,
                resume = resume,
                expectedSalary = expectedSalary,
                availabilityToJoin = availabilityToJoin,
                portfolioLink = portfolioLink,
                additionalComments = additionalComments,
                status = "PENDING",
                appliedAt = appliedAt
            )

            jobApplicationRepository.createJobApplication(application)
            return application
        }

    fun getApplicationById(id: String): JobApplication? {
        return jobApplicationRepository.getApplicationById(id)
    }

    fun listApplicationsForJob(jobId: String): List<JobApplication> {
        return jobApplicationRepository.listApplicationsForJob(jobId)
    }

    fun updateApplicationStatus(id: String, status: String): Boolean {
        return jobApplicationRepository.updateApplicationStatus(id, status)
    }

    fun deleteApplication(id: String): Boolean {
        return jobApplicationRepository.deleteApplication(id)
    }

    fun listAllApplications(): List<JobApplication> {
        return jobApplicationRepository.listAllApplications()
    }

    fun getApplicantsForEmployer(employerId: String): List<JobApplication> {
        return jobApplicationRepository.findApplicantsByEmployerId(employerId)
    }
}
