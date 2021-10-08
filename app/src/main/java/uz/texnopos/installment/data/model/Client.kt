package uz.texnopos.installment.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Client(
    val all_sum: Double,
    val client_id: Int,
    val client_name: String,
    val color: String,
    val count: Int,
    val latter: String,
    val paid: Double,
    val pasport_number: String,
    val pasport_photo: String,
    val pasport_serial: String,
    val phone1: String,
    val phone2: String
): Parcelable