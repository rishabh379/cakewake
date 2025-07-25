package com.pvsrishabh.cakewake.core.data.mapper

import com.pvsrishabh.cakewake.core.data.model.LocationDto
import com.pvsrishabh.cakewake.core.domain.model.Location

fun LocationDto.toDomain(): Location {
    return Location(_id, profileId, locationName, city, state, country, pincode, address, latitude, longitude)
}