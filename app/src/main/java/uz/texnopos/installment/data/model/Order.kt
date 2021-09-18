package uz.texnopos.installment.data.model

data class Order(
    val client_name: String,
    val end_date: String,
    val month: Int,
    val product_name: String,
    val start_date: String
)