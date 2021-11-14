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

var smsDelayValue: Long
    set(value) = getSharedPreferences().setValue(Constants.BULK_SMS_MESSAGE_DELAY_SECONDS, value)
    get() = getSharedPreferences().getLongValue(Constants.BULK_SMS_MESSAGE_DELAY_SECONDS, 4000L)

fun isSignedIn(): Boolean = !token.isNullOrEmpty()
