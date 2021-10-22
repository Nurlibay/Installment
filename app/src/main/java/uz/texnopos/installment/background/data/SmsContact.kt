package uz.texnopos.installment.background.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 10/30/19}
 */

fun String.toSmsContact() =
    SmsContact(this)

@Parcelize
data class SmsContact(
    val contactNumber: String?,
    var isSent: Boolean = false
):Parcelable
