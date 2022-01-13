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
import uz.texnopos.installment.data.model.category.CategoryDetail
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.ApiInterface

class AddOrderViewModel(private val api: ApiInterface): ViewModel() {

    private var _addOrder = MutableLiveData<Resource<GenericResponse<List<PostOrder>>>>()
    val addOrder get() = _addOrder

    private var _getProductsWithCategory = MutableLiveData<Resource<GenericResponse<List<CategoryDetail>>>>()
    val getProductsWithCategory get() = _getProductsWithCategory

    fun addOrder(addOrder: PostOrder) = viewModelScope.launch {
        _addOrder.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val partMap = HashMap<String, RequestBody>()
                addOrder.apply {
                    if (first_pay.isEmpty()) first_pay="0"
                    partMap["product_id"] = product_id.toRequestBody()
                    partMap["client_id"] = client_id.toRequestBody()
                    partMap["first_pay"] = first_pay.toRequestBody()
                    partMap["month"] = month.toRequestBody()
                    partMap["surcharge"] = percent.toRequestBody()
                    partMap["price"] = price.toRequestBody()
                    partMap["description"] = description.toRequestBody()
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

    fun getProductsWithCategory() = viewModelScope.launch {
        _getProductsWithCategory.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getProductsWithCategory()
                if (response.isSuccessful) {
                    if (response.body()!!.successful) {
                        _getProductsWithCategory.value = Resource.success(response.body()!!)
                    } else _getProductsWithCategory.value = Resource.error(response.body()!!.message)
                } else {
                    _getProductsWithCategory.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _getProductsWithCategory.value = Resource.error(e.localizedMessage)
            }
        } else _getProductsWithCategory.value = Resource.networkError()
    }
}