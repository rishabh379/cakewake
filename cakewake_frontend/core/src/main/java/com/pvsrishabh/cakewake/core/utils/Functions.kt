package com.pvsrishabh.cakewake.core.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.pvsrishabh.cakewake.core.domain.model.Country
import java.util.Locale
import com.google.i18n.phonenumbers.PhoneNumberUtil

// Network
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// Country
fun getCountries(): List<Country> {
    return Locale.getISOCountries().map { countryCode ->
        val locale = Locale("", countryCode)
        Country(
            name = locale.displayCountry,
            code = countryCode,
            dialCode = getDialCode(countryCode)
        )
    }.sortedBy { it.name }
}

fun getDialCode(countryCode: String): String {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val supportedRegions = phoneUtil.supportedRegions

    // Generate a mapping of country code to dial code
    val dialCodeMap = supportedRegions.associateWith { regionCode ->
        val dialCode = phoneUtil.getCountryCodeForRegion(regionCode)
        "+$dialCode"
    }

    return dialCodeMap[countryCode] ?: ""
}

fun countryCodeToEmoji(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char ->
            Character.codePointAt(char.toString(), 0) - 0x41 + 0x1F1E6
        }
        .map { codePoint ->
            String(Character.toChars(codePoint))
        }
        .joinToString("")
}
