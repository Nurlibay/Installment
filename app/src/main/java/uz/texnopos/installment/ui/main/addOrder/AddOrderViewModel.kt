package uz.texnopos.installment.ui.main.addOrder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.PostOrder
import uz.texnopos.installment.data.model.Product
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.ApiInterface

class AddOrderViewModel(private val api: ApiInterface): ViewModel() {

    private var _addOrder = MutableLiveData<Resource<GenericResponse<List<PostOrder>>>>()
    val addOrder get() = _addOrder

    private var _getProducts = MutableLiveData<Resource<GenericResponse<List<Product>>>>()
    val getProducts get() = _getProducts

    fun addOrder(addOrder: PostOrder) = viewModelScope.launch {
        _addOrder.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val partMap = HashMap<String, RequestBody>()
                addOrder.apply {
                    partMap["product_id"] = product_id.toRequestBody()
                    partMap["client_id"] = client_id.toRequestBody()
                    partMap["first_pay"] = first_pay.toRequestBody()
                    partMap["month"] = month.toRequestBody()
                    partMap["surcharge"] = surcharge.toRequestBody()
                    partMap["price"] = price.toRequestBody()
                    partMap["product_code"] = product_code.toRequestBody()
                }
                val response = api.addOrder(partMap)
                if (response.isSuccessful) {
                    if (response.body()!!.successful) {
                        _addOrder.value = Resource.success(response.body()!!)
                    } else _addOrder.value = Resource.error(response.body()!!.message)
                } else {
                    _addOrder.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _addOrder.value = Resource.error(e.localizedMessage)
            }
        } else _addOrder.value = Resource.networkError()
    }

    fun getProducts() = viewModelScope.launch {
        _getProducts.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getAllProducts()
                if (response.isSuccessful) {
                    if (response.body()!!.successful) {
                        _getProducts.value = Resource.success(response.body()!!)
                    } else _getProducts.value = Resource.error(response.body()!!.message)
                } else {
                    _getProducts.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _getProducts.value = Resource.error(e.localizedMessage)
            }
        } else _getProducts.value = Resource.networkError()
    }
}