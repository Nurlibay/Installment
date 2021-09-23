package uz.texnopos.installment.core

import uz.texnopos.installment.App
import uz.texnopos.installment.core.Constants.API_TOKEN
import uz.texnopos.installment.core.Constants.IS_LOGGED_IN

fun getSharedPreferences(): SharedPrefUtils {
    return if (App.sharedPrefUtils == null) {
        App.sharedPrefUtils = SharedPrefUtils()
        App.sharedPrefUtils!!
    } else App.sharedPrefUtils!!
}

fun getApiToken() = getSharedPreferences().getStringValue(API_TOKEN)
fun isLoggedIn() = getApiToken().isNotEmpty()

fun clearLoginPref() {

}