package com.pvsrishabh.cakewake.core.domain.usecases.user_entry

import com.pvsrishabh.cakewake.core.domain.manager.LocalUserEntryManager

class ClearUserEntry(
    private val localUserEntryManager: LocalUserEntryManager
) {
    suspend operator fun invoke(id: String) {
        localUserEntryManager.clearUserCredentials(id = id)
    }
}