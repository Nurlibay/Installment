package uz.texnopos.installment.core

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<String>()
    object Loading : Result<Nothing>()
}