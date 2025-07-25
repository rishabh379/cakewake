package com.pvsrishabh.cakewake.core.data.local.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.pvsrishabh.cakewake.core.domain.manager.LocalUserEntryManager
import com.pvsrishabh.cakewake.core.domain.model.User
import com.pvsrishabh.cakewake.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalUserEntryManagerImpl(
    private val context: Context
) : LocalUserEntryManager {

    override suspend fun saveUserCredentials(
        id: String,
        mobileNumber: String,
        isVerified: Boolean,
        profile: String,
        role: String
    ) {
        val credentials = User(
            id = id,
            mobileNumber = mobileNumber,
            isVerified = isVerified,
            profile = profile,
            role = role
        )
        val json = Gson().toJson(credentials)

        context.datastore.edit { settings ->
            settings[UserEntryPreferencesKey.USER_CREDENTIALS] = json
        }
    }

    override fun readUserCredentials(): Flow<User> {
        return context.datastore.data.map { preferences ->
            val json = preferences[UserEntryPreferencesKey.USER_CREDENTIALS] ?: return@map User(
                id = "",
                mobileNumber = "",
                isVerified = false,
                profile = "",
                role = ""
            )
            Gson().fromJson(json, User::class.java)
        }
    }

    override suspend fun clearUserCredentials(id: String) {
        // Just clear the entire saved credentials if UID matches
        val current = context.datastore.data.map {
            it[UserEntryPreferencesKey.USER_CREDENTIALS]
        }.first()

        val stored = current?.let {
            Gson().fromJson(it, User::class.java)
        }

        if (stored?.id == id) {
            context.datastore.edit { settings ->
                settings.remove(UserEntryPreferencesKey.USER_CREDENTIALS)
            }
        }
    }
}

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_SETTINGS)

private object UserEntryPreferencesKey {
    val USER_CREDENTIALS = stringPreferencesKey(name = Constants.USER_CREDENTIALS)
}