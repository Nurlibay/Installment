package uz.texnopos.installment.ui.orders

import uz.texnopos.installment.R
import uz.texnopos.installment.core.BaseFragment

//Created by Alisher 07.09.21
class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
//    private val rotateOpen: Animation by lazy {
//        AnimationUtils.loadAnimation(
//            requireContext(),
//            R.anim.rotate_open_anim
//        )
//    }
//    private val rotateClose: Animation by lazy {
//        AnimationUtils.loadAnimation(
//            requireContext(),
//            R.anim.rotate_close_anim
//        )
//    }
//    private val fromBottom: Animation by lazy {
//        AnimationUtils.loadAnimation(
//            requireContext(),
//            R.anim.from_bottom_anim
//        )
//    }
//    private val toBottom: Animation by lazy {
//        AnimationUtils.loadAnimation(
//            requireContext(),
//            R.anim.to_bottom_anim
//        )
//    }
//
//    private val viewModel by viewModel<OrderViewModel>()
//    private val adapter = OrderAdapter()
//    private var clicked = false
//    private lateinit var navController: NavController
//    private lateinit var bind: FragmentOrdersBinding
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        bind = FragmentOrdersBinding.bind(view)
//        navController = Navigation.findNavController(view)
//
//        bind.apply {
//            rvClients.adapter=adapter
//            adapter.onItemClick {
//
//            }
//            floatingButton.setOnClickListener {
//                onFloatingClicked()
//            }
//            floatingAddButton.setOnClickListener {
//
//            }
//            floatingCalcButton.setOnClickListener {
//               // viewModel.getAllOrders()
//            }
//            viewModel.orders.observe(requireActivity(), {
//                when (it.status) {
//                    ResourseState.LOADING -> showProgress()
//                    ResourseState.SUCCESS -> {
//                        hideProgress()
//                        Log.d(TAG, "onViewCreated: ${it.data}")
//                    }
//                    ResourseState.ERROR -> {
//                        hideProgress()
//                        toast(it.message!!)
//                    }
//                }
//            })
//        }
//    }
//
//    private fun onFloatingClicked() {
//        setAnimation(clicked)
//        setVisibility(clicked)
//        clicked=!clicked
//    }
//
//    private fun setVisibility(clicked: Boolean) {
//        bind.floatingAddButton.isInvisible = clicked
//        bind.floatingCalcButton.isInvisible = clicked
//        if (!clicked) bind.floatingButton.alpha = 1f
//        else bind.floatingButton.alpha = 0.8f
//    }
//
//    private fun setAnimation(clicked: Boolean) {
//        if (!clicked) {
//            bind.floatingAddButton.startAnimation(fromBottom)
//            bind.floatingCalcButton.startAnimation(fromBottom)
//            bind.floatingButton.startAnimation(rotateOpen)
//        } else {
//            bind.floatingAddButton.startAnimation(toBottom)
//            bind.floatingCalcButton.startAnimation(toBottom)
//            bind.floatingButton.startAnimation(rotateClose)
//        }
//    }
//
//
//    override fun onStart() {
//        super.onStart()
//        requireActivity().window.statusBarColor=ContextCompat.getColor(requireContext(),R.color.background_color)
//    }
}