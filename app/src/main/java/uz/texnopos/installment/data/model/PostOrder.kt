package uz.texnopos.installment.data.model

data class PostOrder(
    var product_id: String,
    var client_id: String,
    var first_pay: String,
    var month: String,
    var surcharge: String,
    var price: String,
    var product_code: String
)