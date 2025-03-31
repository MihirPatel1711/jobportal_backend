package com.example.jobportal.job.controller

import com.example.jobportal.job.models.AuthRequest
import com.example.jobportal.job.models.AuthResponse
import com.example.jobportal.job.models.RefreshTokenRequest
import com.example.jobportal.job.models.RegisterRequest
import com.example.jobportal.job.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Map<String, String>> {
        val user = authService.registerUser(request.firstname, request.lastname, request.password, request.email, request.phoneNumber, request.role.toString())
        return ResponseEntity.ok(mapOf("message" to "User registered successfully!", "username" to user.username))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        val tokens = authService.authenticate(request.username, request.password)
        return if (tokens != null) ResponseEntity.ok(tokens) else ResponseEntity.status(401).build()
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<Map<String, String>> {
        val newAccessToken = authService.refreshAccessToken(request)
        return if (newAccessToken != null) {
            ResponseEntity.ok(mapOf("accessToken" to newAccessToken))
        } else {
            ResponseEntity.status(401).body(mapOf("error" to "Invalid or expired refresh token"))
        }
    }



    @PostMapping("/logout")
    fun logout(@RequestBody request: RefreshTokenRequest): ResponseEntity<String> {
        val success = authService.revokeRefreshToken(request.refreshToken)
        return if (success) ResponseEntity.ok("Logged out successfully")
        else ResponseEntity.badRequest().body("Invalid token")
    }
}
