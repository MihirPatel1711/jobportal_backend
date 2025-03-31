package com.example.jobportal.job.service

import com.example.jobportal.job.repository.UserRepository
import com.example.jobportal.models.ListUserInput
import com.example.jobportal.models.User
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserById(id: String): User? {
        return userRepository.findById(id)
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun listUsers(request: ListUserInput): List<User> {
        return userRepository.listUsers(request.search, request.page ?: 1, request.size ?: 10, request.getAll ?: false)
    }

    fun updateUser(user: User): Boolean {
        return userRepository.updateUser(user)
    }

    fun deleteUser(id: String): Boolean {
        return userRepository.deleteUser(id)
    }
}