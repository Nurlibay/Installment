package uz.texnopos.installment.ui.client.pager.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import uz.texnopos.installment.ui.client.pager.fragments.adapter.FragmentAdapter
import uz.texnopos.installment.model.ModelPhoneList
import uz.texnopos.installment.databinding.FragmentThirdBinding

class FragmentThird : Fragment(R.layout.fragment_third) {

    private lateinit var binding: FragmentThirdBinding
    private val adapter = FragmentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)
        binding.rvNext.adapter = adapter
        setData()

    }

    private fun setData() {
        val models: MutableList<ModelPhoneList> = mutableListOf()
        for (i in 1..10) {
            models.add(ModelPhoneList("iPhone 12 Pro Max", "13:55, 12.09.2020г", "+100$"))
        }
        adapter.models = models
    }

}