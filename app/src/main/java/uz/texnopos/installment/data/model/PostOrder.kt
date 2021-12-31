package uz.texnopos.installment.data.model

import com.google.gson.annotations.SerializedName

data class PostOrder(
    var product_id: String,
    var client_id: String,
    var first_pay: String,
    var month: String,
    @SerializedName("surcharge")
    var percent: String,
    var price: String,
    var description: String
)