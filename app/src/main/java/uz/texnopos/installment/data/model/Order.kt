package uz.texnopos.installment.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("first_pay")
    val firstPay: Int,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("paid_sum")
    val paidSum: Double,
    @SerializedName("order_month")
    val orderMonth: Int,
    val status: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: String,
    @SerializedName("start_date")
    val startDate: String,
):Parcelable