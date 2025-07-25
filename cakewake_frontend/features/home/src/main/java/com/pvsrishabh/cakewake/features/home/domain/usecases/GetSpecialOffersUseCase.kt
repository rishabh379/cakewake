package com.pvsrishabh.cakewake.features.home.domain.usecases

import com.pvsrishabh.cakewake.core.utils.Resource
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer
import com.pvsrishabh.cakewake.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpecialOffersUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<SpecialOffer>>> {
        return repository.getSpecialOffers()
    }
}