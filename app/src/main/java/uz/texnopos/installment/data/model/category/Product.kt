package uz.texnopos.installment.data.model.category

data class Product(
    val category_id: Int,
    val created_at: String,
    val del_status: String,
    val id: Int,
    val img_url: String,
    val name: String,
    val updated_at: String
)