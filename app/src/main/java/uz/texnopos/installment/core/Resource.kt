package uz.texnopos.installment.data


open class Resource<out T> constructor(val status: ResourseState, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(ResourseState.SUCCESS, data, null)
        }

        fun <T> error(message: String?): Resource<T> {
            return Resource(ResourseState.ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(ResourseState.LOADING, null, null)
        }
    }
}

enum class ResourseState {
    LOADING, SUCCESS, ERROR
}