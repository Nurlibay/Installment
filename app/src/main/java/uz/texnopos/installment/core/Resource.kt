package uz.texnopos.installment.core


open class Resource<out T> constructor(val status: ResourceState, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(ResourceState.SUCCESS, data, null)
        }

        fun <T> error(message: String?): Resource<T> {
            return Resource(ResourceState.ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(ResourceState.LOADING, null, null)
        }

        fun <T> networkError():Resource<T>{
            return Resource(ResourceState.NETWORK_ERROR,null,null)
        }
    }
}

enum class ResourceState {
    LOADING, SUCCESS, ERROR,NETWORK_ERROR
}