package uz.texnopos.installment.data.model.response

data class GenericResponse<T>(
    var code: Int = 0,
    var message: String = "",
    var payload: T,
    var successful: Boolean
)