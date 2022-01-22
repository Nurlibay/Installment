package uz.texnopos.installment.data.model.category

data class CategoryDetail(
    val category: Category,
    val products: MutableList<Product>
)