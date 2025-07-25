package com.pvsrishabh.cakewake.core.domain.usecases.user

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import com.pvsrishabh.cakewake.core.domain.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User {
        return userRepository.getUser()
    }
}