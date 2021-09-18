package uz.texnopos.installment.ui.client.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.texnopos.installment.ui.client.pager.fragments.FragmentFirst
import uz.texnopos.installment.ui.client.pager.fragments.FragmentSecond
import uz.texnopos.installment.ui.client.pager.fragments.FragmentThird
import uz.texnopos.installment.ui.client.FragmentClient

class ViewPagerAdapter(fragmentClient: FragmentClient) : FragmentStateAdapter(fragmentClient){

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                FragmentFirst()
            }
            1 -> {
                FragmentSecond()
            }
            2 -> {
                FragmentThird()
            }
            else -> {
                Fragment()
            }
        }
    }
}