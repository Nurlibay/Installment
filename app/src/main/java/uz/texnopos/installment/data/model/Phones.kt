package uz.texnopos.installment.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Phones(
    val phone1: @RawValue Any? = null,
    val phone2: @RawValue Any? = null,
    val phone3: @RawValue Any? = null,
    val phone4: @RawValue Any? = null,
    val phone5: @RawValue Any? = null,
    val phone6: @RawValue Any? = null,
    val phone7: @RawValue Any? = null,
    val phone8: @RawValue Any? = null,
    val phone9: @RawValue Any? = null,
    val phone10: @RawValue Any? = null
): Parcelable