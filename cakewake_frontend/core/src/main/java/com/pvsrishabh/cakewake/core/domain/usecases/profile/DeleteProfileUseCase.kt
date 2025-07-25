package com.pvsrishabh.cakewake.core.domain.usecases.profile

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import javax.inject.Inject

class DeleteProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profileId: String): String {
        return userRepository.deleteProfile(profileId)
    }
}