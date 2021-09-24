package uz.texnopos.installment.settings

import android.content.Context
import android.content.SharedPreferences

class Settings (context: Context) {

    companion object {
        const val SIGNED_IN = "signedIn"
        const val TOKEN = "accessToken"
        const val NO_INTERNET = "Интернет не подключен"
        const val UNAUTHORIZED = "Несанкционированный"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences("InstallmentPreferences", Context.MODE_PRIVATE)

    var signedIn: Boolean
        set(value) {
            preferences.edit().putBoolean(SIGNED_IN, value).apply()
        }
        get() = preferences.getBoolean(SIGNED_IN, false)

    var token: String
        set(value) = preferences.edit().putString(TOKEN, value).apply()
        get() = preferences.getString(TOKEN, "") ?: ""
}