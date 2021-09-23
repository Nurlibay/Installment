package uz.texnopos.installment.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private lateinit var childNavController: NavController
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setAppBarCornersRadius()
        childNavController = Navigation.findNavController(requireActivity(), R.id.fcvMainFragment)
        navController = Navigation.findNavController(view)

        binding.apply {
            bnvMain.background = null
            bnvMain.menu.getItem(2).isEnabled = false
            bnvMain.setupWithNavController(childNavController)
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
}