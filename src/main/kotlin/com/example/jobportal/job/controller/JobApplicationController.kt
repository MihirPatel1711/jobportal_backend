package com.example.jobportal.job.controller

import com.example.jobportal.job.models.JobApplication
import com.example.jobportal.job.service.JobApplicationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/applications")
class JobApplicationController(private val jobApplicationService: JobApplicationService) {

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
}
