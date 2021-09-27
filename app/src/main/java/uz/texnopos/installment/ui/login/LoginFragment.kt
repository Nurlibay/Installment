package uz.texnopos.installment.ui.login

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.core.widget.addTextChangedListener
<<<<<<< HEAD
import androidx.core.widget.doAfterTextChanged
=======
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
import androidx.fragment.app.Fragment
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

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setStatusBarColor(R.color.background_blue)
        val settings = Settings(requireContext())
        setUpObserves()
        navController = Navigation.findNavController(view)
        updateUI()
<<<<<<< HEAD
        binding.apply {

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
                    if (validate()) {
                        val login = etLogin.textToString()
                        val password = etPassword.textToString()
                        viewModel.login(PostUser(login, password))
                    }
=======
        bind.apply {
            btnLogin.onClick {
                if (validate()) {
                    val login = etLogin.textToString()
                    val password = etPassword.textToString()
                    viewModel.login(PostUser(login, password))
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
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
            binding.etLogin.checkIsEmpty() -> {
                binding.etLogin.showError(getString(R.string.required_ru))
                false
            }
            binding.etPassword.checkIsEmpty() -> {
                binding.etPassword.showError(getString(R.string.required_ru))
                false
            }
            else -> true
        }
    }

    private fun updateUI() {
        if (isSignedIn()) navController.navigate(R.id.action_loginFragment_to_clientsFragment)
    }

}