package com.pvsrishabh.cakewake.core.data.local.manager

import com.pvsrishabh.cakewake.core.data.local.preferences.UserPreferences
import com.pvsrishabh.cakewake.core.domain.manager.TokenProvider

class TokenProviderImpl(
    private val userPreferences: UserPreferences
) : TokenProvider {

    override suspend fun getToken(): String? {
        return userPreferences.getToken()
    }

    override suspend fun saveToken(token: String) {
        return userPreferences.saveToken(token = token)
    }
}