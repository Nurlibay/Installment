package uz.texnopos.installment.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val end_date: String,
    val first_pay: Int,
    val order_id: Int,
    val status: Int,
    val product_name: String,
    val product_price: String,
    val start_date: String
):Parcelable