package uz.texnopos.installment.data.model

data class Payload(
    val category: Category,
    val products: List<ProductList>
)