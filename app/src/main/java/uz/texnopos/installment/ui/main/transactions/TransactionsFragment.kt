package uz.texnopos.installment.ui.main.transactions

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.model.Transactions
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
    var client: Client? = null
    var order: Order? = null
    var transaction = MutableLiveData<Transactions?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            order = getParcelable(ORDER)
            client = getParcelable(CLIENT)
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
                toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
                transaction.observe(viewLifecycleOwner,{
                    collapsingToolbar.title=order!!.product_name
                    tvClientName.text=client!!.client_name
                    tvOrderId.text=getString(R.string.order_id,order!!.order_id)
                    if (it!=null){
                        progressBar.max=(order!!.product_price.toInt()-order!!.first_pay)/100
                        adapter.models = it.transactions
                        progressBar.progress=it.transactions.sumOf {s->
                            s.paid.toInt() }/100
                        tvNotFound.isVisible=it.transactions.isEmpty()
                        rvOrders.isVisible=it.transactions.isNotEmpty()
                    }
                })
                swipeRefresh.setOnRefreshListener { refresh() }
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
                    transaction.postValue(it.data)
                    hideProgress()
                    bind.swipeRefresh.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    bind.swipeRefresh.isRefreshing = false
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    bind.swipeRefresh.isRefreshing = false
                    hideProgress()
                    toast(Settings.NO_INTERNET)
                }
            }
        }
    }

    private fun showPaymentDialog() {
        PaymentDialog(this)
    }

    fun refresh() {
        viewModel.getTransactions(order!!.order_id)
    }
}