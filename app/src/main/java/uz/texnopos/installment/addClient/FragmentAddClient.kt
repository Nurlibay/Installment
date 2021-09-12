package uz.texnopos.installment.addClient

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentAddClientBinding
import uz.texnopos.installment.repository.Repository

class FragmentAddClient: Fragment(R.layout.fragment_add_client) {

    private lateinit var binding: FragmentAddClientBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: AddClientViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddClientBinding.bind(view)
        navController = Navigation.findNavController(view)
        val repository = Repository()
        val viewModelFactory = AddClientViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddClientViewModel::class.java)
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setStatusBarColor()

        viewModel.getPhoneData()

        viewModel.phoneDataResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful){
                response.body()?.let {
                    //binding.etName.text = response.body().phoneName
                }
            } else {
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setStatusBarColor() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),
            R.color.addclientFragmentStatusBarColor)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
    }
}