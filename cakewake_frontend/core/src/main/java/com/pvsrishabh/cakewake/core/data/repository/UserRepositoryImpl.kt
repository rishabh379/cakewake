// data/repository/UserRepositoryImpl.kt
package com.pvsrishabh.cakewake.core.data.repository

import com.pvsrishabh.cakewake.core.data.mapper.toDomain
import com.pvsrishabh.cakewake.core.data.model.LocationRequest
import com.pvsrishabh.cakewake.core.data.model.ProfileUpdateRequest
import com.pvsrishabh.cakewake.core.data.remote.ApiService
import com.pvsrishabh.cakewake.core.domain.model.Location
import com.pvsrishabh.cakewake.core.domain.model.Profile
import com.pvsrishabh.cakewake.core.domain.model.User
import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUser(): User {
        return apiService.getUser().user.toDomain()
    }

    override suspend fun deleteUser(): String {
        return apiService.deleteUser().message
    }

    override suspend fun getProfile(profileId: String): Profile {
        return apiService.getProfile(profileId).toDomain()
    }

    override suspend fun updateProfile(profileId: String, email: String, name: String, image: String): Profile {
        return apiService.updateProfile(profileId, ProfileUpdateRequest(email, name, image)).profile.toDomain()
    }

    override suspend fun deleteProfile(profileId: String): String {
        return apiService.deleteProfile(profileId).message
    }

    override suspend fun addLocation(profileId: String, location: Location): Location {
        val req = LocationRequest(
            location.latitude, location.longitude, location.locationName,
            location.city, location.state, location.country,
            location.pincode, location.address
        )
        return apiService.addLocation(profileId, req).location.toDomain()
    }

    override suspend fun getLocations(profileId: String): List<Location> {
        return apiService.getLocations(profileId).locations.map { it.toDomain() }
    }

    override suspend fun updateLocation(locationId: String, location: Location): Location {
        val req = LocationRequest(
            location.latitude, location.longitude, location.locationName,
            location.city, location.state, location.country,
            location.pincode, location.address
        )
        return apiService.updateLocation(locationId, req).location.toDomain()
    }

    override suspend fun deleteLocation(locationId: String): String {
        return apiService.deleteLocation(locationId).message
    }
}
