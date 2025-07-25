package com.pvsrishabh.cakewake.core.data.model

data class UserDto(
    val _id: String,
    val mobileNumber: String,
    val isVerified: Boolean,
    val profile: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)
data class SignUpOrLoginResponse(
    val message: String, val isSignup: Boolean
)
data class OtpResponse(val message: String, val token: String, val user: UserDto)
data class UserResponse(val success: Boolean, val user: UserDto)
data class ProfileResponse(val _id: String, val email: String, val name: String, val image: String, val locations: List<LocationDto>)
data class ProfileUpdateRequest(val email: String, val name: String, val image: String)
data class ProfileUpdateResponse(val message: String, val profile: ProfileResponse)

data class LocationRequest(
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val address: String
)
data class LocationDto(
    val _id: String,
    val profileId: String,
    val locationName: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
data class LocationResponse(val success: Boolean, val location: LocationDto)
data class LocationsResponse(val success: Boolean, val locations: List<LocationDto>)
data class BaseResponse(val success: Boolean, val message: String)