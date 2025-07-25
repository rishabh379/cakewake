package com.pvsrishabh.cakewake.core.domain.usecases.location

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(locationId: String): String {
        return userRepository.deleteLocation(locationId)
    }
}