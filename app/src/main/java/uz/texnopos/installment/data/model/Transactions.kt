package uz.texnopos.installment.data.model

data class Transactions(
    val all_debt: Double,
    val amount: String,
    val unprotsent_sum:Double,
    val transactions: MutableList<Transaction>
){
    data class Transaction(
        val created_at: String,
        val id: Int,
        val order_id: Int,
        val paid: String,
        val pay_date: String,
        val updated_at: String
    )
}