package uz.texnopos.installment.addClient

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentAddClientBinding

class FragmentAddClient: Fragment(R.layout.fragment_add_client) {

    private lateinit var binding: FragmentAddClientBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddClientBinding.bind(view)

    }

}