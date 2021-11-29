package uz.texnopos.installment.ui.main.addOrder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.PostOrder
import uz.texnopos.installment.databinding.AddOrderDialogBinding

class AddOrderFragment : Fragment(R.layout.add_order_dialog) {

    private lateinit var binding: AddOrderDialogBinding
    private val viewModel: AddOrderViewModel by viewModel()
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = arguments?.getParcelable(Constants.CLIENT)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddOrderDialogBinding.bind(view)
        setUpObserver()
        binding.apply {
            toolbar.navOnClick {
                requireActivity().onBackPressed()
            }
            btnAddOrder.onClick {
                if (validate()) {
                    val newOrder = PostOrder(
                        product_id = etProductId.textToString(),
                        client_id = etClientId.textToString(),
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

    private fun setUpObserver() {
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
            etProductId.checkIsEmpty() -> etProductId.showError(getString(R.string.required))
            etClientId.checkIsEmpty() -> etClientId.showError(getString(R.string.required))
            etFirstPay.checkIsEmpty() -> etFirstPay.showError(getString(R.string.required))
            etMonth.checkIsEmpty() -> etMonth.showError(getString(R.string.required))
            etSurcharge.checkIsEmpty() -> etSurcharge.showError(getString(R.string.required))
            etPrice.checkIsEmpty() -> etPrice.showError(getString(R.string.required))
            etProductCode.checkIsEmpty() -> etProductCode.showError(getString(R.string.required))
            else -> true
        }
    }
}