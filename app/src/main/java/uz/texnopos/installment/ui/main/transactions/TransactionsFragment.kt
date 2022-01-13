package uz.texnopos.installment.ui.main.transactions

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.Constants.CLIENT
import uz.texnopos.installment.core.Constants.ORDER
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.model.Transactions
import uz.texnopos.installment.databinding.FragmentTransactionsBinding
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
//        showProgress()
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.background_blue)
        setUpObservers()
        navController = Navigation.findNavController(view)
        bind = FragmentTransactionsBinding.bind(view)
            .apply {
                scrollView.setOnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY) postPayment.hide()
                    else postPayment.show()
                }
                toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
                transaction.observe(viewLifecycleOwner, {
                    postPayment.isInvisible = false
                    toolbar.title = order!!.productName
                    tvClientName.text = client!!.clientName
                    tvOrderId.text = getString(R.string.order_id, order!!.orderId)
                    if (it != null) {
                        val tranSum=it.transactions.sumOf { s ->
                            s.paid.toInt()
                        }
                        val initialLoan=it.allDebt+tranSum
                        tvPaidSum.text=tranSum.toLong().changeFormat()
                        tvAllDebt.text=initialLoan.changeFormat()
                        progressBar.max = (initialLoan / 100).toInt()
                        it.transactions.add(Transactions.Transaction(
                            createdAt = "",
                            id=0,
                            payDate = order!!.startDate+" ПерваяОплата :99",
                            orderId = order!!.orderId,
                            paid = order!!.firstPay.toDouble(),
                            updatedAt = ""
                        ))
                        adapter.models = it.transactions
                        progressBar.progress = tranSum/100
                        tvNotFound.isVisible = it.transactions.isEmpty()
                        rvTransactions.isVisible = it.transactions.isNotEmpty()
                    }
                })

                rvTransactions.adapter = adapter

                postPayment.onClick { showPaymentDialog() }

                toolbar.navOnClick { requireActivity().onBackPressed() }
            }
    }

    private fun setUpObservers() {
        viewModel.transactions.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    transaction.postValue(it.data)
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        }
    }

    private fun showPaymentDialog() {
        PaymentDialog(this)
    }

    fun refresh() {
        viewModel.getTransactions(order!!.orderId)
    }
}