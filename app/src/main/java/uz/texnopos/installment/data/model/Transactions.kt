package uz.texnopos.installment.data.model

data class Transactions(
    val all_debt: Double,
    val amount: String,
    val transactions: MutableList<Transaction>
)