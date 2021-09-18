package uz.texnopos.installment.data


open class Resource<out T> constructor(val status: LoadingState, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(LoadingState.SUCCESS, data, null)
        }

        fun <T> error(message: String?): Resource<T> {
            return Resource(LoadingState.ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(LoadingState.LOADING, null, null)
        }
    }
}

enum class LoadingState {
    LOADING, SUCCESS, ERROR
}