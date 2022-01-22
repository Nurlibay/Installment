package uz.texnopos.installment.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.onClick
import uz.texnopos.installment.databinding.BottomSheetBinding
import uz.texnopos.installment.databinding.DialogBottomSheetBinding
import uz.texnopos.installment.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    private lateinit var childNavController: NavController
    lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        childNavController = Navigation.findNavController(requireActivity(), R.id.fcvMainFragment)
        navController = Navigation.findNavController(view)

        setAppBarCornersRadius()
        setUpObservers()
        binding.apply {
            bnvMain.setupWithNavController(childNavController)
            bnvMain.background = null
            bnvMain.menu.getItem(2).isEnabled = false
            if (
                childNavController.currentDestination?.id == R.id.homeFragment ||
                childNavController.currentDestination?.id == R.id.clientsFragment ||
                childNavController.currentDestination?.id == R.id.productFragment ||
                childNavController.currentDestination?.id == R.id.settingsFragment
            ) {
//                bnvMain.menu.getItem(4).setOnMenuItemClickListener {
//                    val action = MainFragmentDirections.actionMainFragmentToMenuAddition()
//                    navController.navigate(action)
//                    return@setOnMenuItemClickListener true
//                }
            }
            fabMain.onClick {
                when (childNavController.currentDestination?.id) {
                    R.id.homeFragment -> {
                        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
                        val v = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
                        val dialogBottomSheetBinding = DialogBottomSheetBinding.bind(v)
                        bottomSheetDialog.setContentView(v)
                        bottomSheetDialog.show()

                        dialogBottomSheetBinding.apply {
                            tvAddNewClient.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addClientFragment)
                            }
                            tvAddNewProduct.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addProductFragment)
                            }
                        }
                    }
                    R.id.clientsFragment -> {
                        navController.navigate(R.id.action_mainFragment_to_addClientFragment)
                    }
                    R.id.productFragment -> {
                        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
                        val v = layoutInflater.inflate(R.layout.bottom_sheet, null)
                        val bottomSheetBinding = BottomSheetBinding.bind(v)
                        bottomSheetDialog.setContentView(v)
                        bottomSheetDialog.show()

                        bottomSheetBinding.apply {
                            tvAddNewCategory.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addCategoryFragment)
                            }
                            tvAddNewProduct.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addProductFragment)
                            }
                        }
                    }
                    R.id.settingsFragment -> {
                        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
                        val v = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
                        val dialogBottomSheetBinding = DialogBottomSheetBinding.bind(v)
                        bottomSheetDialog.setContentView(v)
                        bottomSheetDialog.show()

                        dialogBottomSheetBinding.apply {
                            tvAddNewClient.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addClientFragment)
                            }
                            tvAddNewProduct.onClick {
                                bottomSheetDialog.dismiss()
                                navController.navigate(R.id.action_mainFragment_to_addProductFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setLoading(load: Boolean) {
        binding.apply {
            pbLoading.isVisible = load
            bnvMain.isVisible = !load
            fabMain.isVisible = !load
            babMain.isVisible = !load
        }
    }

    private fun setAppBarCornersRadius() {
        val radius = resources.getDimension(R.dimen.margin_standard)
        val bottomBarBackground = binding.babMain.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel =
            bottomBarBackground.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
    }

    private fun setUpObservers() {
//        viewModel.refresh.observe(viewLifecycleOwner) {
//            when (it.status) {
//                ResourceState.LOADING -> setLoading(true)
//                ResourceState.SUCCESS -> {
//                    setLoading(false)
//                    settings.token = it.data!!.token
//                    settings.signedIn = true
//                }
//                ResourceState.ERROR -> {
//                    settings.signedIn = false
//                    showMessage(view?.context!!.getString(R.string.sign_in_again_ru))
//                    navController.navigate(R.id.action_mainFragment_to_signInFragment)
//                }
//            }
//        }
    }
}