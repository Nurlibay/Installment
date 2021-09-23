package uz.texnopos.installment.ui.main.pager.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import uz.texnopos.installment.ui.main.pager.fragments.adapter.FragmentAdapter
import uz.texnopos.installment.data.model.ModelPhoneList
import uz.texnopos.installment.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    private lateinit var binding: FragmentFirstBinding
    private val adapter = FragmentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)
        binding.rvPrevious.adapter = adapter
        setData()
    }

    private fun setData() {
        val models : MutableList<ModelPhoneList> = mutableListOf()
        for (i in 1..10){
            models.add(ModelPhoneList("iPhone 12 Pro Max", "13:55, 12.09.2020г", "+100$"))
        }
        adapter.models = models
    }

}