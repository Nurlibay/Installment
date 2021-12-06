package uz.texnopos.installment.ui.main.addOrder

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.gms.common.util.CollectionUtils.listOf
import okhttp3.internal.immutableListOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.PostOrder
import uz.texnopos.installment.data.model.Product
import uz.texnopos.installment.databinding.AddOrderDialogBinding

class AddOrderFragment : Fragment(R.layout.add_order_dialog) {

    private lateinit var binding: AddOrderDialogBinding
    private val viewModel: AddOrderViewModel by viewModel()
    private var clientId: Int = 0
    private var productList: List<Product> = listOf()
    private var productNames: MutableList<String> = mutableListOf()
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientId = arguments?.getInt("client_id")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddOrderDialogBinding.bind(view)
        viewModel.getProducts()
        viewModel.getProducts.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    productList = it.data!!.payload
                    productList.forEach {
                        productNames.add(it.product_name)
                    }
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

        val arrayAdapter = ArrayAdapter(requireContext(),
            R.layout.item_drop_down,
            productList.map { it.product_name })
        binding.productName.setAdapter(arrayAdapter)
        binding.productName.setOnItemClickListener { parent, view, position, id ->
            productId = productList[position].product_id
            toast(productId.toString())
            toast(binding.productName.toString())
        }
        setUpObserverOrder()
        binding.apply {
            toolbar.navOnClick {
                requireActivity().onBackPressed()
            }
            btnAddOrder.onClick {
                if (validate()) {
                    val newOrder = PostOrder(
                        product_id = productId.toString(),
                        client_id = clientId.toString(),
                        first_pay = etFirstPay.textToString(),
                        month = etMonth.textToString(),
                        surcharge = etSurcharge.textToString(),
                        price = etPrice.textToString(),
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

    private fun AddOrderDialogBinding.validate(): Boolean {
        return when {
            etFirstPay.checkIsEmpty() -> etFirstPay.showError(getString(R.string.required))
            etMonth.checkIsEmpty() -> etMonth.showError(getString(R.string.required))
            etSurcharge.checkIsEmpty() -> etSurcharge.showError(getString(R.string.required))
            etPrice.checkIsEmpty() -> etPrice.showError(getString(R.string.required))
            etProductCode.checkIsEmpty() -> etProductCode.showError(getString(R.string.required))
            else -> true
        }
    }
}