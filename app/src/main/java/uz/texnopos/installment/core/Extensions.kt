package uz.texnopos.installment.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.data.Client
import java.io.File


fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (context != null) {
        context!!.toast(text, duration)
    }
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

inline fun <T : Toolbar> T.navOnClick(crossinline func: T.() -> Unit) =
    setNavigationOnClickListener { func() }

fun TextInputEditText.textToString() = this.text.toString()
fun TextView.textToString() = this.text.toString()

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


fun TextInputEditText.showError(error: String): Boolean {
    this.error = error
    this.showSoftKeyboard()
    return false
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


fun Client.toSmsText(smsText: String): String {
    return smsText
        .replace("{first_name}", firstName)
        .replace("{last_name}", lastName)
        .replace("{magazin}", "2-magazin")
        .replace("{amount}", amount)
        .replace("{end_date}", endDate)
}


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

fun Long.changeFormat(): String {
    val num = this.toString()
    var s = ""
    val sz = num.length
    for (i in 0 until sz) {
        if (i != 0 && (i - sz % 3) % 3 == 0) s += ' '
        s += num[i]
    }
    return "$s сум"
}

fun Double.changeFormat(): String {
    val num = this.toInt().toString()
    var s = ""
    val sz = num.length
    for (i in 0 until sz) {
        if (i != 0 && (i - sz % 3) % 3 == 0) s += ' '
        s += num[i]
    }
    return "$s сум"
}

fun String.getOnlyDigits(): String {
    var s = ""
    this.forEach { if (it.isDigit()) s += it }
    return s
}

const val cacheSize = (5 * 1024 * 1024).toLong()
val myCache = Cache(getAppInstance().cacheDir, cacheSize)

fun <T> callApi(
    call: Call<T>,
    onApiSuccess: (T?) -> Unit = {},
    onApiError: (errorMsg: String) -> Unit = {},
) {
    Log.d("api_calling", call.request().url.toString())
    call.enqueue(object : retrofit2.Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when {
                response.isSuccessful -> onApiSuccess.invoke(response.body())
                else -> {
                    onApiError.invoke(response.errorBody().toString())
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

fun Client.toSmsContact() = Client(amount, endDate, firstName, lastName, phone1, phone2)


fun deleteCache(context: Context) {
    try {
        val dir: File = context.cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
    }
}

fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) dir.delete() else {
        false
    }
}

fun String.changeDateFormat(): String {
    var s = ""
    val date = this.dropLast(3).split(' ', '-')
    s += "${date[2]} "
    s += when (date[1]) {
        "01" -> "Янв "
        "02" -> "Февр "
        "03" -> "Мар "
        "04" -> "Апр "
        "05" -> "Мая "
        "06" -> "Июн "
        "07" -> "Июл "
        "08" -> "Авг "
        "09" -> "Сент "
        "10" -> "Окт "
        "11" -> "Нояб "
        "12" -> "Дек "
        else -> ""
    }
    s += "${date[0]} "
    s += date[3]
    return s
}

fun String.changeDateFormat2(): String {
    var s = ""
    val date = this.split('-')
    s += "${date[2]} "
    s += when (date[1]) {
        "01" -> "Янв "
        "02" -> "Февр "
        "03" -> "Мар "
        "04" -> "Апр "
        "05" -> "Мая "
        "06" -> "Июн "
        "07" -> "Июл "
        "08" -> "Авг "
        "09" -> "Сент "
        "10" -> "Окт "
        "11" -> "Нояб "
        "12" -> "Дек "
        else -> ""
    }
    s += "${date[0]} "
    return s
}

fun String.toRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun File.toRequestBody(): RequestBody =
    this.asRequestBody("image/*".toMediaTypeOrNull())

fun File.toMultiPart(key: String) =
    MultipartBody.Part.createFormData(key, this.name, this.toRequestBody())

fun Fragment.launchBrowser(link: String) {
    try {
        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(myIntent)
    } catch (e: ActivityNotFoundException) {
        toast("Ни одно приложение не может обработать этот запрос \nПожалуйста, установите веб-браузер")
        e.printStackTrace()
    }
}