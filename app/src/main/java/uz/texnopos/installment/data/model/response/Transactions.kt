package uz.texnopos.installment.data.model.response

data class Transactions(
    val end_date: String,
    val month: Int,
    val product_name: String,
    val start_date: String
)