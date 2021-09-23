package uz.texnopos.installment.data.model

data class LoginResponse(
    val email: String? = null,
    val password: String? = null,
    val token: String? = null
)