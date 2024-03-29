package uz.texnopos.installment.ui.main.orders

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.FragmentOrdersBinding
import uz.texnopos.installment.core.Constants.CLIENT
import uz.texnopos.installment.core.Constants.NO_INTERNET
import uz.texnopos.installment.core.Constants.ORDER

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    private val viewModel: OrdersViewModel by viewModel()
    private val orderAdapter = OrdersAdapter()
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = arguments?.getParcelable(CLIENT)
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
        binding = FragmentOrdersBinding.bind(view).apply {

            //tvClientPhone.text = client!!.phone1
            tvClientId.text = getString(R.string.client_id, client!!.clientId)
            tvProductCount.text = getString(R.string.count_product, client!!.count)
            collapsingToolbar.title = client!!.clientName
            swipeRefresh.setOnRefreshListener {
                refresh()
            }
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemCall -> {
                        if (client != null) {
                            val bundle = Bundle()
                            bundle.putParcelable(CLIENT, client)
                            try {
                                navController.navigate(R.id.action_ordersFragment_to_callDialog, bundle)
                            } catch (e: Exception) {
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

            orderAdapter.onItemClick {
                val bundle = Bundle()
                bundle.putParcelable(CLIENT, client)
                bundle.putParcelable(ORDER, it)
                try {
                    navController.navigate(R.id.action_clientFragment_to_clientTransactionsFragment,
                        bundle)
                } catch (e: Exception) {
                }
            }
            rvOrders.adapter = orderAdapter
            btnFab.onClick {
                val bundle = Bundle()
                bundle.putInt("client_id", client!!.clientId)
                navController.navigate(R.id.action_ordersFragment_to_addOrderFragment, bundle)
            }

            rvOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && btnFab.isVisible) {
                        btnFab.hide()
                    } else if (dy < 0 && btnFab.visibility != View.VISIBLE) {
                        btnFab.show()
                    }
                }
            })
        }
    }

    private fun refresh() {
        viewModel.getOrders(client!!.clientId)
    }
    
    private fun setUpObservers() {
        viewModel.orders.observe(requireActivity()) {
            when (it.status) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    orderAdapter.setData(it.data!!)
                    binding.apply {
                        tvNotFound.isVisible = it.data.isEmpty()
                        swipeRefresh.isRefreshing = false
                    }
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }
}