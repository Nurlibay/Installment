package uz.texnopos.installment.ui.main.addClient

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentAddClientBinding

class AddClientFragment: Fragment(R.layout.fragment_add_client) {

    private lateinit var binding: FragmentAddClientBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddClientBinding.bind(view)
        navController = Navigation.findNavController(view)
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setStatusBarColor()

    }
    
    private fun setStatusBarColor() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),
            R.color.addClientFragmentStatusBarColor)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
    }
}