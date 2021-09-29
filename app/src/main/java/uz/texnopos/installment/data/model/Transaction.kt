package uz.texnopos.installment.data.model

data class Transaction(
    val created_at: String,
    val id: Int,
    val order_id: Int,
    val paid: String,
    val pay_date: String,
    val updated_at: String
)