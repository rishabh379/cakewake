package com.pvsrishabh.cakewake.core.domain.usecases.location

import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import com.pvsrishabh.cakewake.core.domain.model.Location
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(locationId: String, location: Location): Location {
        return userRepository.updateLocation(locationId, location)
    }
}