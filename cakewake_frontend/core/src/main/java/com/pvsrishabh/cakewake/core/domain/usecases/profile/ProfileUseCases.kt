package com.pvsrishabh.cakewake.core.domain.usecases.profile

data class ProfileUseCases(
    val getProfile: GetProfileUseCase,
    val updateProfile: UpdateProfileUseCase,
    val deleteProfile: DeleteProfileUseCase
)