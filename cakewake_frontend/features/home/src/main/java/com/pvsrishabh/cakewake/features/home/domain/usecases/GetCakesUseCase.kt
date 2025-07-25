package com.pvsrishabh.cakewake.features.home.domain.usecases

import androidx.paging.PagingData
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCakesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(
        category: String? = null,
        budgetMin: Int? = null,
        budgetMax: Int? = null,
        search: String? = null
    ): Flow<PagingData<Cake>> {
        return repository.getCakes(
            category = category,
            budgetMin = budgetMin,
            budgetMax = budgetMax,
            search = search
        )
    }
}