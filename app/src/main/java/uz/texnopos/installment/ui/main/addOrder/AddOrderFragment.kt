package uz.texnopos.installment.ui.main.addOrder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.mask.MaskWatcherPrice
import uz.texnopos.installment.data.model.PostOrder
import uz.texnopos.installment.data.model.category.CategoryDetail
import uz.texnopos.installment.databinding.FragmentAddOrderBinding

class AddOrderFragment : Fragment(R.layout.fragment_add_order) {

    private lateinit var binding: FragmentAddOrderBinding
    private val viewModel: AddOrderViewModel by viewModel()
    private var clientId: Int = 0
    private var productId: Int = -1
    private var categoryId: Int = -1
    private var productNameList = listOf<String?>()
    private val productsWithCategory = MutableLiveData<List<CategoryDetail>?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientId = arguments?.getInt("client_id")!!
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProductsWithCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddOrderBinding.bind(view)
        setUpObserverGetOrder()
        setUpObserverProductsWithCategory()
        binding.apply {
            toolbar.navOnClick {
                requireActivity().onBackPressed()
            }

            etFirstPay.addTextChangedListener(MaskWatcherPrice(etFirstPay))
            etPrice.addTextChangedListener(MaskWatcherPrice(etPrice))

            productsWithCategory.observe(requireActivity(), { categoryDetail ->
                val categoryNameList = categoryDetail?.map { it.category.name }!!.toTypedArray()
                val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, categoryNameList)
                categoryName.setAdapter(categoryAdapter)
                categoryName.setOnItemClickListener { _, _, position, _ ->
                    productNameList = categoryDetail[position].products.map { productName -> productName.name }
                    val productAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, productNameList)
                    productName.setAdapter(productAdapter)

                    // product list Item change listener
                    productName.setOnItemClickListener { _, _, pos, _ ->
                        val productName = productName.textToString()
                        categoryDetail[position].products.forEach {
                            if (productName == it.name) productId = it.id
                        }
                    }
                }
            })

            btnAddOrder.onClick {
                Timber.d("producId", productId.toString())
                if (validate()) {
                    toast(productId.toString())
                    Timber.d("producId", productId.toString())
                    val newOrder = PostOrder(
                        product_id = productId.toString(),
                        client_id = clientId.toString(),
                        first_pay = etFirstPay.textToString().getOnlyDigits(),
                        month = etMonth.textToString(),
                        percent = etSurcharge.textToString(),
                        price = etPrice.textToString().getOnlyDigits(),
                        description = etDescription.textToString()
                    )
                    viewModel.addOrder(newOrder)
                }
            }
        }
    }

    private fun setUpObserverGetOrder() {
        viewModel.addOrder.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    toast(it.data!!.message)
                    requireActivity().onBackPressed()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        })
    }

    private fun setUpObserverProductsWithCategory() {
        viewModel.getProductsWithCategory.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> {}
                ResourceState.SUCCESS -> {
                    if (it.data != null) productsWithCategory.postValue(it.data.payload)
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    toast(Constants.NO_INTERNET)
                }
            }
        })
    }

    private fun FragmentAddOrderBinding.validate(): Boolean {
        return when {
            productName.checkIsEmpty() || productId == -1 -> {
                toast("Этот продукт вам недоступен")
                productName.showError(getString(R.string.required))
            }
            etFirstPay.checkIsEmpty() -> tilFirstPay.showError(getString(R.string.required))
            etMonth.checkIsEmpty() -> tilMonth.showError(getString(R.string.required))
            etSurcharge.checkIsEmpty() -> tilSurcharge.showError(getString(R.string.required))
            etPrice.checkIsEmpty() -> tilPrice.showError(getString(R.string.required))
            else -> true
        }
    }
}