package com.pvsrishabh.cakewake.core.data.mapper

import com.pvsrishabh.cakewake.core.data.model.UserDto
import com.pvsrishabh.cakewake.core.domain.model.User

fun UserDto.toDomain(): User {
    return User(_id, mobileNumber, isVerified, profile, role)
}