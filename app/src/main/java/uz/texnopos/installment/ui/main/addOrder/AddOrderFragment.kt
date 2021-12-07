package uz.texnopos.installment.ui.main.addOrder

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.mask.MaskWatcherPrice
import uz.texnopos.installment.data.model.PostOrder
import uz.texnopos.installment.data.model.Product
import uz.texnopos.installment.databinding.FragmentAddOrderBinding

class AddOrderFragment : Fragment(R.layout.fragment_add_order) {

    private lateinit var binding: FragmentAddOrderBinding
    private val viewModel: AddOrderViewModel by viewModel()
    private var clientId: Int = 0
    private var productId: Int = -1
    private val products = MutableLiveData<List<Product>?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientId = arguments?.getInt("client_id")!!
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddOrderBinding.bind(view)
        setUpObserverOrder()
        observeProducts()
        binding.apply {
            toolbar.navOnClick {
                requireActivity().onBackPressed()
            }

            etFirstPay.addTextChangedListener(MaskWatcherPrice(etFirstPay))
            etPrice.addTextChangedListener(MaskWatcherPrice(etPrice))
            products.observe(requireActivity(), {
                if (it != null) {
                    val productNames = it.map { p ->
                        p.product_name
                    }
                    val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down,
                        productNames)
                    productName.setAdapter(arrayAdapter)
                }
            })

            btnAddOrder.onClick {
                val writtenProduct = productName.textToString()
                products.value.let {
                    it?.map { p -> p.product_name }.let { n ->
                        val index = n!!.indexOf(writtenProduct)
                        if (index != -1) productId = it!![index].product_id
                        else index
                    }
                }
                if (validate()) {
                    val newOrder = PostOrder(
                        product_id = productId.toString(),
                        client_id = clientId.toString(),
                        first_pay = etFirstPay.textToString().getOnlyDigits(),
                        month = etMonth.textToString(),
                        surcharge = etSurcharge.textToString(),
                        price = etPrice.textToString().getOnlyDigits(),
                        product_code = etProductCode.textToString()
                    )
                    viewModel.addOrder(newOrder)
                }
            }
        }
    }

    private fun setUpObserverOrder() {
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

    private fun observeProducts() {
        viewModel.getProducts.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> {}
                ResourceState.SUCCESS -> {
                    if (it.data != null) products.postValue(it.data.payload)
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
            productName.checkIsEmpty()||productId==-1-> {
                toast("Этот продукт вам недоступен")
                productName.showError(getString(R.string.required))
            }
            etFirstPay.checkIsEmpty() -> etFirstPay.showError(getString(R.string.required))
            etMonth.checkIsEmpty() -> etMonth.showError(getString(R.string.required))
            etSurcharge.checkIsEmpty() -> etSurcharge.showError(getString(R.string.required))
            etPrice.checkIsEmpty() -> etPrice.showError(getString(R.string.required))
            etProductCode.checkIsEmpty() -> etProductCode.showError(getString(R.string.required))
            else -> true
        }
    }
}