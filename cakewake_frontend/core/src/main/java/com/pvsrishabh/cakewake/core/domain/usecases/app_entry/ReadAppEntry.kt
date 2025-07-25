package com.pvsrishabh.cakewake.core.domain.usecases.app_entry

import com.pvsrishabh.cakewake.core.domain.manager.LocalAppEntryManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
    private val localAppEntryManager: LocalAppEntryManager
) {
    operator fun invoke(): Flow<Boolean>{
        return localAppEntryManager.readAppEntry()
    }
}