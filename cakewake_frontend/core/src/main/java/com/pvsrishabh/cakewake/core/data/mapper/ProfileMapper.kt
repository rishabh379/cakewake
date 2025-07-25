package com.pvsrishabh.cakewake.core.data.mapper

import com.pvsrishabh.cakewake.core.data.model.ProfileResponse
import com.pvsrishabh.cakewake.core.domain.model.Profile

fun ProfileResponse.toDomain(): Profile {
    return Profile(_id, email, name, image, locations.map { it.toDomain() })
}