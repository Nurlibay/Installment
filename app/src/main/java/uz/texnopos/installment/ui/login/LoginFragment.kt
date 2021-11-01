package uz.texnopos.installment.ui.login

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.razir.progressbutton.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.preferences.isSignedIn
import uz.texnopos.installment.core.preferences.token
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.databinding.FragmentLoginBinding
import uz.texnopos.installment.settings.Constants.NO_INTERNET

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
            bindProgressButton(btnLogin)
            btnLogin.attachTextChangeAnimator {
                fadeInMills = 300
                fadeOutMills = 300
            }

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
                ResourceState.LOADING -> bind.btnLogin.showProgress(true)
                ResourceState.SUCCESS -> {
                    bind.btnLogin.hideProgress(getString(R.string.success))

                    token = it.data!!.payload.token
                    Handler(Looper.getMainLooper()).postDelayed({
                        bind.btnLogin.showProgress(false)
                        updateUI()
                    },2000)
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    bind.btnLogin.hideProgress(R.string.btn_login)
                    bind.btnLogin.showProgress(false)
                }
                ResourceState.NETWORK_ERROR -> {
                    bind.btnLogin.hideProgress(R.string.btn_login)
                    bind.btnLogin.showProgress(false)
                    toast(NO_INTERNET)
                }
            }
        })
    }

    private fun validate(): Boolean {
        return when {
            bind.etLogin.checkIsEmpty() -> {
                bind.etLogin.showError(getString(R.string.required))
                false
            }
            bind.etPassword.checkIsEmpty() -> {
                bind.etPassword.showError(getString(R.string.required))
                false
            }
            else -> true
        }
    }

    private fun updateUI() {
        if (isSignedIn()) navController.navigate(R.id.action_loginFragment_to_clientsFragment)
    }

    private fun Button.showProgress(isLoading: Boolean) {
        bind.inputLogin.isEnabled=!isLoading
        bind.inputPassword.isEnabled=!isLoading
        this.isEnabled = !isLoading
        if (isLoading) {
            this.showProgress {
                progressColor = Color.WHITE
                gravity = DrawableButton.GRAVITY_CENTER
            }
        }
    }
}