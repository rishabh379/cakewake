package com.pvsrishabh.cakewake.core.domain.usecases.user_entry

import com.pvsrishabh.cakewake.core.domain.manager.LocalUserEntryManager

class SaveUserEntry (
    private val localUserEntryManager: LocalUserEntryManager
) {
    suspend operator fun invoke(id: String,
                                mobileNumber: String,
                                isVerified: Boolean,
                                profile: String,
                                role: String) {
        localUserEntryManager.saveUserCredentials(
            id = id,
            mobileNumber = mobileNumber,
            isVerified = isVerified,
            profile = profile,
            role = role)
    }
}