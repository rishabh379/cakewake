package com.pvsrishabh.cakewake.core.data.local.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.pvsrishabh.cakewake.core.domain.manager.LocalAppEntryManager
import com.pvsrishabh.cakewake.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalAppEntryManagerImpl (
    private val context: Context
) : LocalAppEntryManager {

    override suspend fun saveAppEntry() {
        context.datastore.edit { settings ->
            settings[AppEntryPreferencesKey.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.datastore.data.map { preferences ->
            preferences[AppEntryPreferencesKey.APP_ENTRY] ?: false
        }
    }
}

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.APP_SETTINGS)

private object AppEntryPreferencesKey {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
}