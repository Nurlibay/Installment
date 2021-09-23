package uz.texnopos.installment.core

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, text, duration).show()

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    if (context != null) {
        context!!.toast(text, duration)
    }
}

//inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

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
    if (visibility) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
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

fun View.onClick(onClick: (View) -> Unit) {
    this.setOnClickListener(onClick)
}

//@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
//fun isNetworkAvailable(): Boolean {
//    val info = getAppInstance().getConnectivityManager().activeNetworkInfo
//    return info != null && info.isConnected
//}

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


fun <T> callApi(
    call: Call<T>,
    onApiSuccess: (T?) -> Unit = {},
    onApiError: (errorMsg: String) -> Unit = {}
) {
    Log.d("api_calling", call.request().url.toString())
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when {
                response.isSuccessful -> onApiSuccess.invoke(response.body())
                else -> {
                    onApiError.invoke(
                        when (response.code()) {
                            401 -> "Unauthorized"
                            else -> response.errorBody().toString()
                        }
                    )
                    Log.d("api-failure", response.errorBody().toString())
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onApiError.invoke(t.localizedMessage!!)
            Log.d("api-failure", t.localizedMessage!!)
        }

    })
}