package com.pvsrishabh.cakewake.features.home.domain.usecases

import com.pvsrishabh.cakewake.core.utils.Resource
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Category>>> {
        return repository.getCategories()
    }
}