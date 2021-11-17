package uz.texnopos.installment.ui.main.orders

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.background.util.askPermission
import uz.texnopos.installment.background.util.isHasPermission
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.FragmentOrdersBinding
import uz.texnopos.installment.core.Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
import uz.texnopos.installment.core.Constants.CLIENT
import uz.texnopos.installment.core.Constants.NO_INTERNET
import uz.texnopos.installment.core.Constants.ORDER

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    private val viewModel: OrdersViewModel by viewModel()
    private val adapter = OrdersAdapter()
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = arguments?.getParcelable(CLIENT)
//        showProgress()
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

            tvClientPhone.text = client!!.phone1
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
                            makePhoneCall(client!!.phone1)
                        }
                        true
                    }
                    else -> false
                }
            }

            adapter.onItemClick {
                val bundle = Bundle()
                bundle.putParcelable(CLIENT, client)
                bundle.putParcelable(ORDER, it)
                try {
                    navController.navigate(R.id.action_clientFragment_to_clientTransactionsFragment,
                        bundle)
                } catch (e: Exception) {
                }
            }
            rvOrders.adapter = adapter
            btnFab.onClick {
                showAddorderDialog()
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

    private fun showAddorderDialog() {

    }

    private fun makePhoneCall(phone: String) {
        if (isHasPermission(CALL_PHONE)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")
            startActivity(callIntent)
        } else askPermission(arrayOf(CALL_PHONE), ASK_PHONE_PERMISSION_REQUEST_CODE)

    }

    private fun refresh() {
        viewModel.getOrders(client!!.clientId)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == ASK_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (client != null) {
                    makePhoneCall(client!!.phone1)
                }
            } else {
                toast("PERMISSION DENIED")
            }
        }
    }
    
    private fun setUpObservers() {
        viewModel.orders.observe(requireActivity()) {
            when (it.status) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    adapter.setData(it.data!!)
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