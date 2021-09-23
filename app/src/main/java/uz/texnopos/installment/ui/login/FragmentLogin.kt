package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.base.BaseFragment
import uz.texnopos.installment.core.Constants.API_TOKEN
import uz.texnopos.installment.core.Constants.TAG
import uz.texnopos.installment.core.getSharedPreferences
import uz.texnopos.installment.core.isLoggedIn
import uz.texnopos.installment.core.textToString
import uz.texnopos.installment.core.toast
import uz.texnopos.installment.data.LoadingState
import uz.texnopos.installment.data.model.LoginResponse
import uz.texnopos.installment.databinding.FragmentLoginBinding

class FragmentLogin : BaseFragment(R.layout.fragment_login) {

    private lateinit var bind: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<LoginViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentLoginBinding.bind(view)
        setUpObserves()
        navController = Navigation.findNavController(view)
        updateUI()
        bind.apply {
            btnLogin.setOnClickListener {
                val login = LoginResponse(etLogin.textToString(), etPassword.textToString())
                viewModel.signWithLogin(login)
            }
        }
    }

    private fun setUpObserves() {
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                LoadingState.LOADING -> showProgress()
                LoadingState.SUCCESS -> {
                    val token = it.data!!.token
                    hideProgress()
                    getSharedPreferences().setValue(API_TOKEN, token)
                    updateUI()
                }
                LoadingState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.loginFragmentStatusBarColor)
    }

    private fun updateUI() {
        if (isLoggedIn()) {
            navController.navigate(R.id.action_fragmentLogin_to_clientsFragment)
        }
    }
}