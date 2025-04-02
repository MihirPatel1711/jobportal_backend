package com.example.jobportal.job.controller

import com.example.jobportal.job.models.Job
import com.example.jobportal.job.models.ListJobsRequest
import com.example.jobportal.job.service.JobService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/jobs")
class JobController(private val jobService: JobService) {

    @PostMapping("/create")
    fun createJob(@RequestBody job: Job): ResponseEntity<Job> {
        val newJob = jobService.createJob(job.title, job.description, job.company, job.location, job.salary, job.remote, job.jobType, job.requiredSkills, job.educationLevel, job.languageRequirement, job.postedBy)
        return ResponseEntity.ok(newJob)
    }

    @GetMapping("/get/{id}")
    fun getJobById(@PathVariable id: String): ResponseEntity<Job> {
        val job = jobService.getJobById(id)
        return if (job != null) ResponseEntity.ok(job) else ResponseEntity.notFound().build()
    }

    @PostMapping("/list")
    fun listJobs(@RequestBody request: ListJobsRequest): ResponseEntity<List<Job>> {
        return ResponseEntity.ok(jobService.listJobs(request))
    }

    @PostMapping("/update")
    fun updateJob(@RequestBody job: Job): ResponseEntity<Map<String, Any>> {
        val success = jobService.updateJob(job)
        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "Job updated successfully"))
        } else {
            ResponseEntity.badRequest().body(mapOf("success" to false, "message" to "Job not found"))
        }
    }


    @DeleteMapping("/delete/{id}")
    fun deleteJob(@PathVariable id: String): ResponseEntity<String> {
        val success = jobService.deleteJob(id)
        return if (success) ResponseEntity.ok("Job deleted successfully") else ResponseEntity.badRequest().body("Job not found")
    }
}
