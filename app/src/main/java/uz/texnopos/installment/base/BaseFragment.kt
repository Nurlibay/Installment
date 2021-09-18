package uz.texnopos.installment.base

import androidx.fragment.app.Fragment

open class BaseFragment(contentLayout: Int) : Fragment(contentLayout) {
    fun hideProgress() {
        if (activity != null)
            (requireActivity() as AppBaseActivity).showProgress(false)
    }

    fun showProgress() {
        if (activity != null)
            (requireActivity() as AppBaseActivity).showProgress(true)
    }
}