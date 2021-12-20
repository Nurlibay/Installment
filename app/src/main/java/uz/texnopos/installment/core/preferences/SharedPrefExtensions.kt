package uz.texnopos.installment.core.preferences

import uz.texnopos.installment.App
import uz.texnopos.installment.core.Constants

fun getSharedPreferences(): SharedPrefUtils {
    return if (App.sharedPrefUtils == null) {
        App.sharedPrefUtils = SharedPrefUtils()
        App.sharedPrefUtils!!
    } else App.sharedPrefUtils!!
}

var token: String?
    set(value) = getSharedPreferences().setValue(Constants.TOKEN, value)
    get() = getSharedPreferences().getStringValue(Constants.TOKEN)

fun isSignedIn(): Boolean = !token.isNullOrEmpty()
