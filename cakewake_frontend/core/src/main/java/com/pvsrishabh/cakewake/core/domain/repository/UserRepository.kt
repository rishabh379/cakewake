package com.pvsrishabh.cakewake.core.domain.repository

import com.pvsrishabh.cakewake.core.domain.model.Location
import com.pvsrishabh.cakewake.core.domain.model.Profile
import com.pvsrishabh.cakewake.core.domain.model.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun deleteUser(): String

    suspend fun getProfile(profileId: String): Profile
    suspend fun updateProfile(profileId: String, email: String, name: String, image: String): Profile
    suspend fun deleteProfile(profileId: String): String

    suspend fun addLocation(profileId: String, location: Location): Location
    suspend fun getLocations(profileId: String): List<Location>
    suspend fun updateLocation(locationId: String, location: Location): Location
    suspend fun deleteLocation(locationId: String): String
}
