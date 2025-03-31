package com.example.jobportal.job.service

import com.example.jobportal.job.models.Job
import com.example.jobportal.job.models.ListJobsRequest
import com.example.jobportal.job.repository.JobRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class JobService(private val jobRepository: JobRepository) {

    fun createJob(title: String, description: String, company: String, location: String, salary: Double, remote: Boolean, jobType: String, requiredSkills: List<String>, educationLevel: String?, languageRequirement: String?, postedBy: String): Job {
        val id = UUID.randomUUID().toString()
        val createdAt = System.currentTimeMillis()
        val updatedAt = createdAt

        val job = Job(id, title, description, company, location, salary, remote, jobType, requiredSkills, educationLevel, languageRequirement, postedBy, createdAt, updatedAt)
        jobRepository.createJob(job)
        return job
    }

    fun getJobById(id: String): Job? {
        return jobRepository.getJobById(id)
    }

    fun listJobs(request: ListJobsRequest): List<Job> {
        return jobRepository.listJobs(request)
    }

    fun updateJob(job: Job): Boolean {
        val updatedJob = job.copy(updatedAt = System.currentTimeMillis())
        return jobRepository.updateJob(updatedJob)
    }

    fun deleteJob(id: String): Boolean {
        return jobRepository.deleteJob(id)
    }
}
