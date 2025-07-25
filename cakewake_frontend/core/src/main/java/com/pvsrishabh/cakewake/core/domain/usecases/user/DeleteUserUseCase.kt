package com.pvsrishabh.cakewake.core.domain.usecases.user

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        return userRepository.deleteUser()
    }
}