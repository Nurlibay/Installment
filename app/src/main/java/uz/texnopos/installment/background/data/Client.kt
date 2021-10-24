package uz.texnopos.installment.background.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(
    val amount: String,
    val end_date: String,
    val first_name: String,
    val last_name: String,
    val phone1: String,
    val phone2: String,
    var isSent1: Boolean = false,
    var isSent2: Boolean = false,
) : Parcelable