package uz.texnopos.installment.ui.main.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.category.CategoryDetail
import uz.texnopos.installment.data.retrofit.ApiInterface

class ProductViewModel(private val api: ApiInterface): ViewModel() {

    private var _getCategory: MutableLiveData<Resource<List<CategoryDetail>>> = MutableLiveData()
    val getCategory get() = _getCategory

    fun getCategories() = viewModelScope.launch {
        _getCategory.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getProductsWithCategory()
                if (response.isSuccessful) {
                    if (response.body()!!.successful) {
                        _getCategory.value = Resource.success(response.body()!!.payload)
                    } else _getCategory.value = Resource.error(response.body()!!.message)
                } else {
                    _getCategory.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _getCategory.value = Resource.error(e.localizedMessage)
            }
        } else _getCategory.value = Resource.networkError()
    }

}