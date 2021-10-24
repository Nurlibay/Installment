package uz.texnopos.installment.data.model

import com.google.gson.annotations.SerializedName

data class Transactions(
    @SerializedName("all_debt")
    val allDebt: Double,
    val amount: Double,
    @SerializedName("unprotsent_sum")
    val withoutRate:Double,
    val status: Int,
    val transactions: MutableList<Transaction>
){
    data class Transaction(
        @SerializedName("created_at")
        val createdAt: String,
        val id: Int,
        @SerializedName("order_id")
        val orderId: Int,
        val paid: Double,
        @SerializedName("pay_date")
        val payDate: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}