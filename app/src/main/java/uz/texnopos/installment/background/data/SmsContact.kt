package uz.texnopos.installment.background.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

fun String.toSmsContact() =
    SmsContact(this)

@Parcelize
data class SmsContact(
    val contactNumber: String?,
    var isSent: Boolean = false

):Parcelable
