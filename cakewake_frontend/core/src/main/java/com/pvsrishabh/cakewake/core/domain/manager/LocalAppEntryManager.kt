package com.pvsrishabh.cakewake.core.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalAppEntryManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
}
