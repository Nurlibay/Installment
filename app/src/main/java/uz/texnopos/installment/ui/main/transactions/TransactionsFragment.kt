package uz.texnopos.installment.ui.main.transactions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.core.ResourceState
import uz.texnopos.installment.core.toast
import uz.texnopos.installment.core.*
import uz.texnopos.installment.databinding.FragmentTransactionsBinding
import uz.texnopos.installment.settings.Settings
import uz.texnopos.installment.ui.main.payment.PaymentDialog

class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val adapter = TransactionsAdapter()
    private lateinit var bind: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModel()
    private lateinit var navController: NavController
    var orderId:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId=arguments?.getInt("orderId")!!
        setUpObservers()
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.background_blue)
        navController = Navigation.findNavController(view)
        bind = FragmentTransactionsBinding.bind(view).apply {
            rvOrders.adapter = adapter
            postPayment.setOnClickListener {
                showPaymentDialog()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.transactions.observe(requireActivity()) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    adapter.models=it.data!!.toMutableList()
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Settings.NO_INTERNET)
                }
            }
        }
    }
    private fun showPaymentDialog(){
        val dialog=PaymentDialog(this)
        dialog.apply {
            orderId=this@TransactionsFragment.orderId
        }


    }
     fun refresh(){
        viewModel.getTransactions(orderId!!)
    }
}