package uz.texnopos.installment.ui.main.transactions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.databinding.FragmentTransactionsBinding
import uz.texnopos.installment.settings.Settings
import uz.texnopos.installment.settings.Settings.Companion.CLIENT
import uz.texnopos.installment.settings.Settings.Companion.ORDER
import uz.texnopos.installment.ui.main.payment.PaymentDialog

class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val adapter = TransactionsAdapter()
    private lateinit var bind: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModel()
    private lateinit var navController: NavController
    var client:Client?=null
    var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            order=getParcelable(ORDER)
            client=getParcelable(CLIENT)
        }
    }

    override fun onStart() {
        super.onStart()
        showProgress()
        refresh()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.background_blue)
        setUpObservers()
        navController = Navigation.findNavController(view)
        bind = FragmentTransactionsBinding.bind(view)
            .apply {
                collapsingToolbar.title=order!!.product_name
                tvClientName.text=client!!.client_name
                tvClientPhone.text=client!!.phone1
                progressBar.max=order!!.product_price.toInt()-order!!.first_pay
                container.setOnRefreshListener { refresh() }
                rvOrders.adapter = adapter
                postPayment.onClick {
                    showPaymentDialog()
                }

                toolbar.setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
            }
    }

    private fun setUpObservers() {
        viewModel.transactions.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> { }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    bind.container.isRefreshing = false
                    adapter.models = it.data!!.toMutableList()
                    bind.progressBar.progress=it.data.sumOf { p->
                        p.paid.toInt()
                    }
                }
                ResourceState.ERROR -> {
                    bind.container.isRefreshing = false
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    bind.container.isRefreshing = false
                    hideProgress()
                    toast(Settings.NO_INTERNET)
                }
            }
        }
    }
    private fun showPaymentDialog(){
        PaymentDialog(this)
    }
     fun refresh(){
         viewModel.getTransactions(order!!.order_id)
    }
}