package uz.texnopos.installment.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Phone(
    val phone: String = "",
): Parcelable