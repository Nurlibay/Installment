package uz.texnopos.installment.core.preferences

import android.content.Context
import android.content.SharedPreferences
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.core.Constants.mySharedPreferences


class SharedPrefUtils {
     private val mSharedPreferences: SharedPreferences = getAppInstance()
        .getSharedPreferences(mySharedPreferences, Context.MODE_PRIVATE)
    private var mSharedPreferencesEditor: SharedPreferences.Editor = mSharedPreferences.edit()

    init {
        mSharedPreferencesEditor.apply()
    }

    fun setValue(key: String, value: Any?) {
        when (value) {
            is Int? -> {
                mSharedPreferencesEditor.putInt(key, value!!).apply()
            }
            is Float? -> {
                mSharedPreferencesEditor.putFloat(key, value!!).apply()
            }
            is String? -> {
                mSharedPreferencesEditor.putString(key, value).apply()
            }
            is Long? -> {
                mSharedPreferencesEditor.putLong(key, value!!).apply()
            }
            is Boolean? -> {
                mSharedPreferencesEditor.putBoolean(key, value!!).apply()
            }
            is ArrayList<*>->{
                mSharedPreferencesEditor.putStringSet(key, (value as ArrayList<String>).toSet()).apply()
            }
        }
    }

    fun getStringValue(key: String, defaultValue: String = ""): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun getLongValue(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun getBooleanValue(keyFlag: String, defaultValue: Boolean = false): Boolean {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue)
    }
    fun getStringSetValue(keyFlag: String,defaultValue: Set<String> = setOf()):Set<String>?{
        return mSharedPreferences.getStringSet(keyFlag,defaultValue)
    }

    fun removeKey(key: String) {
        mSharedPreferencesEditor.remove(key).apply()
    }

    fun clear() {
        mSharedPreferencesEditor.clear().apply()
    }
}