package com.example.jobportal.job.controller

import com.example.jobportal.job.service.UserService
import com.example.jobportal.models.ListUserInput
import com.example.jobportal.models.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    // ✅ Get User by ID
    @GetMapping("/get/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user) else ResponseEntity.notFound().build()
    }

    @GetMapping("/get-by-username/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.getUserByUsername(username)
        return if (user != null) ResponseEntity.ok(user) else ResponseEntity.notFound().build()
    }

    // ✅ List Users with Search & Pagination
    @PostMapping("/list")
    fun listUsers(@RequestBody request: ListUserInput): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.listUsers(request))
    }

    // ✅ Update User
    @PostMapping("/update")
    fun updateUser(@RequestBody user: User): ResponseEntity<String> {
        val success = userService.updateUser(user)
        return if (success) ResponseEntity.ok("User updated successfully") else ResponseEntity.badRequest().body("User not found")
    }

    // ✅ Delete User
    @DeleteMapping("/delete/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<String> {
        val success = userService.deleteUser(id)
        return if (success) ResponseEntity.ok("User deleted successfully") else ResponseEntity.badRequest().body("User not found")
    }
}