package uz.texnopos.installment.client

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import uz.texnopos.installment.R
import uz.texnopos.installment.client.pager.ViewPagerAdapter
import uz.texnopos.installment.databinding.FragmentClientBinding

class FragmentClient: Fragment(R.layout.fragment_client) {

    private lateinit var binding: FragmentClientBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientBinding.bind(view)
        val adapter = ViewPagerAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager){ tab, position ->
            when(position){
                0 -> {
                    tab.text = "пред месяц"
                }
                1 -> {
                    tab.text = "текущии месяц"
                }
                2 -> {
                    tab.text = "след месяц"
                }
            }
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor= ContextCompat.getColor(requireContext(),R.color.item_background)
    }
}