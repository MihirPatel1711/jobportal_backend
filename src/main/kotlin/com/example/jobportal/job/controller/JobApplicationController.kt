package com.example.jobportal.job.controller

import com.example.jobportal.job.models.JobApplication
import com.example.jobportal.job.service.JobApplicationService
import com.example.jobportal.job.utils.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/applications")
class JobApplicationController(private val jobApplicationService: JobApplicationService,
                               private val jwtUtil: JwtUtil
) {

    @PostMapping("/apply")
    fun applyForJob(@RequestBody application: JobApplication): ResponseEntity<JobApplication> {
        val newApplication = jobApplicationService.applyForJob(
            jobId = application.jobId,
            applicantId = application.applicantId,
            firstname = application.firstname,
            lastname = application.lastname,
            phoneNumber = application.phoneNumber,
            resume = application.resume,
            expectedSalary = application.expectedSalary,
            availabilityToJoin = application.availabilityToJoin,
            portfolioLink = application.portfolioLink,
            additionalComments = application.additionalComments
        )
        return ResponseEntity.ok(newApplication)
    }

    @GetMapping("/get/{id}")
    fun getApplicationById(@PathVariable id: String): ResponseEntity<JobApplication> {
        val application = jobApplicationService.getApplicationById(id)
        return if (application != null) ResponseEntity.ok(application) else ResponseEntity.notFound().build()
    }

    @GetMapping("/list/{jobId}")
    fun listApplicationsForJob(@PathVariable jobId: String): ResponseEntity<List<JobApplication>> {
        return ResponseEntity.ok(jobApplicationService.listApplicationsForJob(jobId))
    }

    @PostMapping("/update-status/{id}")
    fun updateApplicationStatus(@PathVariable id: String, @RequestParam status: String): ResponseEntity<Map<String, Any>> {
        val success = jobApplicationService.updateApplicationStatus(id, status)
        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "Application status updated successfully"))
        } else {
            ResponseEntity.badRequest().body(mapOf("success" to false, "message" to "Application not found"))
        }
    }

    @DeleteMapping("/delete/{id}")
    fun deleteApplication(@PathVariable id: String): ResponseEntity<String> {
        val success = jobApplicationService.deleteApplication(id)
        return if (success) ResponseEntity.ok("Application deleted successfully") else ResponseEntity.badRequest().body("Application not found")
    }

    @GetMapping("/list")
    fun listAllApplications(): List<JobApplication> {
        return jobApplicationService.listAllApplications()
    }


    @GetMapping("/my-job-applicants")
    fun getApplicantsForMyJobs(request: HttpServletRequest): ResponseEntity<List<JobApplication>> {
        val token = extractToken(request) ?: return ResponseEntity.status(401).build()

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).build() // Unauthorized if token is invalid
        }

        val userId = jwtUtil.extractUserId(token) ?: return ResponseEntity.status(401).build()
        val applications = jobApplicationService.getApplicantsForEmployer(userId)
        return ResponseEntity.ok(applications)
    }

    // Extract JWT token from Authorization header
    private fun extractToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        return if (authHeader.startsWith("Bearer ")) authHeader.substring(7) else null
    }

}
