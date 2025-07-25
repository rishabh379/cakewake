package com.pvsrishabh.cakewake.core.domain.usecases.app_entry

import com.pvsrishabh.cakewake.core.domain.manager.LocalAppEntryManager

class SaveAppEntry(
    private val localAppEntryManager: LocalAppEntryManager
) {
    suspend operator fun invoke(){
        localAppEntryManager.saveAppEntry()
    }
}