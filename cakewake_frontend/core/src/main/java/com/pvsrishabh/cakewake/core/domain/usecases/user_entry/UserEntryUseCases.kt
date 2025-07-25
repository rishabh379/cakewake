package com.pvsrishabh.cakewake.core.domain.usecases.user_entry

data class UserEntryUseCases(
    val readUserEntry: ReadUserEntry,
    val saveUserEntry: SaveUserEntry,
    val clearUserEntry: ClearUserEntry,
)