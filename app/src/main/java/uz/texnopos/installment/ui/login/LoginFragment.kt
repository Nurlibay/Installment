package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.databinding.FragmentLoginBinding
import uz.texnopos.installment.settings.Settings
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModel()
    private val settings: Settings by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setUpObserves()
        navController = Navigation.findNavController(view)

        if (settings.signedIn) {
            navController.navigate(R.id.action_loginFragment_to_clientsFragment)
        }

        binding.apply {
            etLogin.addTextChangedListener {
                tilLogin.isErrorEnabled = false
            }
            etPassword.doAfterTextChanged {
                tilPassword.isErrorEnabled = false
            }
            btnLogin.onClick {
                val login = etLogin.textToString()
                val password = etPassword.textToString()
                if (login != "" && password != "") {
                    viewModel.login(PostUser(login, password))
                } else {
                    if (login == "") tilLogin.error = getString(R.string.required_ru)
                    if (password == "") tilPassword.error = getString(R.string.required_ru)
                }
            }
        }
        hintVisible()
    }

    private fun setUpObserves() {
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    settings.token = it.data!!.payload.token
                    settings.signedIn = true
                    navController.navigate(R.id.action_loginFragment_to_clientsFragment)
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR-> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }


    private fun hintVisible() {
        binding.apply {
            tilLogin.hint = getString(R.string.login_ru)
            tilLogin.setOnFocusChangeListener { _, b ->
                if (b) {
                    tilLogin.hint = ""
                    tilLogin.isErrorEnabled = false
                } else {
                    tilLogin.hint = getString(R.string.login_ru)
                }
            }
            tilPassword.hint = getString(R.string.password_ru)
            tilPassword.setOnFocusChangeListener { _, b ->
                if (b) {
                    tilPassword.hint = ""
                    tilPassword.isErrorEnabled = false
                } else {
                    tilPassword.hint = getString(R.string.password_ru)
                }
            }
        }
    }
}