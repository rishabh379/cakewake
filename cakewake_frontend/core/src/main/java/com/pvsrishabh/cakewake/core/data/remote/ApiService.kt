package com.pvsrishabh.cakewake.core.data.remote

import com.pvsrishabh.cakewake.core.data.model.BaseResponse
import com.pvsrishabh.cakewake.core.data.model.LocationRequest
import com.pvsrishabh.cakewake.core.data.model.LocationResponse
import com.pvsrishabh.cakewake.core.data.model.LocationsResponse
import com.pvsrishabh.cakewake.core.data.model.ProfileResponse
import com.pvsrishabh.cakewake.core.data.model.ProfileUpdateRequest
import com.pvsrishabh.cakewake.core.data.model.ProfileUpdateResponse
import com.pvsrishabh.cakewake.core.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // User
    @GET("user")
    suspend fun getUser(): UserResponse

    @DELETE("user")
    suspend fun deleteUser(): BaseResponse

    // Profile
    @GET("profile/{id}")
    suspend fun getProfile(@Path("id") profileId: String): ProfileResponse

    @PUT("profile/{id}")
    suspend fun updateProfile(
        @Path("id") profileId: String,
        @Body body: ProfileUpdateRequest
    ): ProfileUpdateResponse

    @DELETE("profile/{id}")
    suspend fun deleteProfile(@Path("id") profileId: String): BaseResponse

    // Location
    @POST("location/{profileId}")
    suspend fun addLocation(
        @Path("profileId") profileId: String,
        @Body body: LocationRequest
    ): LocationResponse

    @GET("location/{profileId}")
    suspend fun getLocations(@Path("profileId") profileId: String): LocationsResponse

    @PUT("location/{locationId}")
    suspend fun updateLocation(
        @Path("locationId") locationId: String,
        @Body body: LocationRequest
    ): LocationResponse

    @DELETE("location/{locationId}")
    suspend fun deleteLocation(@Path("locationId") locationId: String): BaseResponse
}