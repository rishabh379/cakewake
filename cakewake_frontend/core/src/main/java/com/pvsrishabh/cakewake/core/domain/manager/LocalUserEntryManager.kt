package com.pvsrishabh.cakewake.core.domain.manager

import com.pvsrishabh.cakewake.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LocalUserEntryManager {
    suspend fun saveUserCredentials(
        id: String,
        mobileNumber: String,
        isVerified: Boolean,
        profile: String,
        role: String
    )
    suspend fun clearUserCredentials(id: String)
    fun readUserCredentials(): Flow<User>
}
