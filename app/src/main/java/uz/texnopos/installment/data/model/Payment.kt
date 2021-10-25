package uz.texnopos.installment.data.model

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("order_id")
    val orderId:Int,
    val amount:Double
)