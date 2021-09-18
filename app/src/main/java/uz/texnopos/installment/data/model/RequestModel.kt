package uz.texnopos.installment.data.model

data class RequestModel<T>(
    val successful: Boolean,
    val code: Int,
    val message: String,
    val payload: T
)