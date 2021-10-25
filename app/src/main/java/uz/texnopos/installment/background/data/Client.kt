package uz.texnopos.installment.background.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(
    val amount: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone1: String,
    val phone2: String,
    var isSent1: Boolean = false,
    var isSent2: Boolean = false,
) : Parcelable