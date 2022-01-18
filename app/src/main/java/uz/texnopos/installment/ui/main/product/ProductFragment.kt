package uz.texnopos.installment.ui.main.product

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.category.Category
import uz.texnopos.installment.databinding.FragmentProductBinding

class ProductFragment: Fragment(R.layout.fragment_product){

    private lateinit var binding: FragmentProductBinding
    private val adapter = CategoryAdapter()
    private val viewModel: ProductViewModel by viewModel()
    private val categoryList = mutableListOf<Category>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBinding.bind(view).apply {
            rvCategory.adapter = adapter
        }
        viewModel.getCategories()
        setUpObserver()
        adapter.setOnCategoryItemClickListener {

        }
    }

    private fun setUpObserver() {
        viewModel.getCategory.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> { }
                ResourceState.SUCCESS -> {
                    it.data!!.forEach { category ->
                        categoryList.add(category.category)
                        adapter.models = categoryList
                    }
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        })
    }
}