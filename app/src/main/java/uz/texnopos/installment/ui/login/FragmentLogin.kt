package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentLoginBinding

class FragmentLogin : Fragment(R.layout.fragment_login){

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        navController = Navigation.findNavController(view)
        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_fragmentLogin_to_clientsFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor= ContextCompat.getColor(requireContext(),R.color.loginFragmentStatusBarColor)
    }

}