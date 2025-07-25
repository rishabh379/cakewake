package com.pvsrishabh.cakewake.core.domain.usecases.user_entry

import com.pvsrishabh.cakewake.core.domain.manager.LocalUserEntryManager

class ReadUserEntry(
    private val localUserEntryManager: LocalUserEntryManager
) {
    operator fun invoke() = localUserEntryManager.readUserCredentials()
}