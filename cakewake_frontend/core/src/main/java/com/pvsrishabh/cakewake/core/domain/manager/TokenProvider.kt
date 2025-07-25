package com.pvsrishabh.cakewake.core.domain.manager

interface TokenProvider {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
}