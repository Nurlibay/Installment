package uz.texnopos.installment.core

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.Cache
import uz.texnopos.installment.App
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.core.preferences.SharedPrefUtils
import uz.texnopos.installment.settings.Settings.Companion.TOKEN

fun getSharedPreferences(): SharedPrefUtils {
    return if (App.sharedPrefUtils == null) {
        App.sharedPrefUtils = SharedPrefUtils()
        App.sharedPrefUtils!!
    } else App.sharedPrefUtils!!
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (context != null) {
        context!!.toast(text, duration)
    }
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

fun TextInputEditText.textToString() = this.text.toString()
fun TextView.textToString() = this.text.toString()

fun Fragment.isGPSEnable(): Boolean =
    context!!.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun TextInputEditText.checkIsEmpty(): Boolean = text == null ||
        textToString() == "" ||
        textToString().equals("null", ignoreCase = true)

fun TextInputEditText.showError(error: String) {
    this.error = error
    this.showSoftKeyboard()
}

fun View.visibility(visibility: Boolean): View {
    this.isVisible = visibility
    return this
}

fun View.enabled(isEnabled: Boolean): View {
    this.isEnabled = isEnabled
    return this
}

fun Fragment.showMessage(msg: String?) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun ViewGroup.inflate(@LayoutRes id: Int): View =
    LayoutInflater.from(context).inflate(id, this, false)

fun RecyclerView.addVertDivider(context: Context?) {
    this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}

fun RecyclerView.addHorizDivider(context: Context?) {
    this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
}

fun Fragment.setStatusBarColor(colorId: Int) {
    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
}

fun isNetworkAvailable(): Boolean {
    val info = getAppInstance().getConnectivityManager().activeNetworkInfo
    return info != null && info.isConnected
}

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

var token: String
    set(value) = getSharedPreferences().setValue(TOKEN, value)
    get() = getSharedPreferences().getStringValue(TOKEN)

fun isSignedIn(): Boolean = token.isNotEmpty()

fun Fragment.showProgress() {
    (requireActivity() as AppBaseActivity).showProgress(true)
}

fun Fragment.hideProgress() {
    (requireActivity() as AppBaseActivity).showProgress(false)
}

fun String.contains2(s: String): Boolean {
    return this.lowercase().contains(s.lowercase())
}

fun String.changeFormat(): String {
    var s = ""
    val sz = this.length
    for (i in 0 until sz) {
        if (i != 0 && (i - sz % 3) % 3 == 0) s += ' '
        s += this[i]
    }
    return "$s сум"
}

const val cacheSize = (5 * 1024 * 1024).toLong()
val myCache = Cache(getAppInstance().cacheDir, cacheSize)