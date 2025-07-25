package com.pvsrishabh.cakewake.features.home.data.mapper

import com.pvsrishabh.cakewake.features.home.data.remote.dto.CakeDto
import com.pvsrishabh.cakewake.features.home.data.remote.dto.CategoryDto
import com.pvsrishabh.cakewake.features.home.data.remote.dto.SpecialOfferDto
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer

fun CakeDto.toDomain(): Cake {
    return Cake(
        id = id,
        name = name,
        price = price,
        originalPrice = originalPrice,
        imageUrl = imageUrl,
        discount = discount,
        categoryId = categoryId,
        isFeatured = isFeatured,
        rating = rating,
        description = description
    )
}

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        imageUrl = imageUrl,
        color = color
    )
}

fun SpecialOfferDto.toDomain(): SpecialOffer {
    return SpecialOffer(
        id = id,
        title = title,
        description = description,
        cakes = cakes.map { it.toDomain() },
        backgroundColor = backgroundColor
    )
}