package uz.texnopos.installment.data.model

data class Amount(
    var productPrice: Double = 0.0,
    var firstPay: Double = 0.0,
    var percent: Int = 0,
    var month: Int = 0
)