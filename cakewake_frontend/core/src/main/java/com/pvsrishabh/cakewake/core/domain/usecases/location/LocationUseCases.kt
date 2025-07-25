package com.pvsrishabh.cakewake.core.domain.usecases.location

data class LocationUseCases(
    val addLocation: AddLocationUseCase,
    val getLocations: GetLocationsUseCase,
    val updateLocation: UpdateLocationUseCase,
    val deleteLocation: DeleteLocationUseCase
)