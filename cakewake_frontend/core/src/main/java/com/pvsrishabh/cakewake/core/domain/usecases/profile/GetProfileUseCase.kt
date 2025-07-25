package com.pvsrishabh.cakewake.core.domain.usecases.profile

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import com.pvsrishabh.cakewake.core.domain.model.Profile
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profileId: String): Profile {
        return userRepository.getProfile(profileId)
    }
}