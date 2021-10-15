package uz.texnopos.installment.data.model

data class Amount(
    var product_price: Double = 0.0,
    var first_pay: Double = 0.0,
    var percent: Int = 0,
    var month: Int = 0
)