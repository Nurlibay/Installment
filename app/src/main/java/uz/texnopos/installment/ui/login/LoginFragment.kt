package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.databinding.FragmentLoginBinding
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var bind: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentLoginBinding.bind(view)
        setStatusBarColor(R.color.background_blue)
        setUpObserves()
        navController = Navigation.findNavController(view)
        updateUI()
        bind.apply {
            btnLogin.onClick {
                if (validate()) {
                    val login = etLogin.textToString()
                    val password = etPassword.textToString()
                    viewModel.login(PostUser(login, password))
                }
            }
        }
    }

    private fun setUpObserves() {
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    token = it.data!!.payload.token
                    updateUI()
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }

    private fun validate(): Boolean {
        return when {
            bind.etLogin.checkIsEmpty() -> {
                bind.etLogin.showError(getString(R.string.required_ru))
                false
            }
            bind.etPassword.checkIsEmpty() -> {
                bind.etPassword.showError(getString(R.string.required_ru))
                false
            }
            else -> true
        }
    }

    private fun updateUI() {
        if (isSignedIn()) navController.navigate(R.id.action_loginFragment_to_clientsFragment)
    }


}