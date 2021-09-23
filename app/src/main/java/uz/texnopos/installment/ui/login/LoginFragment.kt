package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.BaseFragment
import uz.texnopos.installment.core.showMessage
import uz.texnopos.installment.core.textToString
import uz.texnopos.installment.data.ResourseState
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.databinding.FragmentLoginBinding
import uz.texnopos.installment.settings.Settings

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
            navController.navigate(R.id.action_loginFragment_to_ordersFragment)
        }

        binding.apply {
            btnLogin.setOnClickListener {
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
    }

    private fun setUpObserves() {
        viewModel.user.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourseState.LOADING -> {
                    isLoading(true)
                }
                ResourseState.SUCCESS -> {
                    isLoading(false)
                    settings.token = it.data!!.payload.token
                    settings.signedIn = true
                    navController.navigate(R.id.action_loginFragment_to_ordersFragment)
                }
                ResourseState.ERROR -> {
                    showMessage(it.message)
                    isLoading(false)
                }
            }
        })
    }

    private fun isLoading(load: Boolean) {
        binding.apply {
            loading.isVisible = load
        }
    }
}