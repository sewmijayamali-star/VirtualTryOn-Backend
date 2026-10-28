package com.ZeRo.service

import com.ZeRo.data.repository.UserRepository


class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(
        name: String,
        email: String,
        passwordHash: String
    ): Int {
        return userRepository.createUser(
            name = name,
            email = email,
            passwordHash = passwordHash
        )
    }

    fun findUserByEmail(email: String): Triple<Int, String, String>? {
        return userRepository.findUserByEmail(email)
    }
}