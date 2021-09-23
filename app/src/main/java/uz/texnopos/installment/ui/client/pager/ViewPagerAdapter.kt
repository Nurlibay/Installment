package uz.texnopos.installment.ui.client.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.installment.ui.client.pager.fragments.FirstFragment
import uz.texnopos.installment.ui.client.pager.fragments.SecondFragment
import uz.texnopos.installment.ui.client.pager.fragments.ThirdFragment
import uz.texnopos.installment.ui.client.ClientFragment

class ViewPagerAdapter(clientFragment: ClientFragment) : FragmentStateAdapter(clientFragment){

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                FirstFragment()
            }
            1 -> {
                SecondFragment()
            }
            2 -> {
                ThirdFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}