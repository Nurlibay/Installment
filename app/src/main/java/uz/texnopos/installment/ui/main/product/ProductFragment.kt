package uz.texnopos.installment.ui.main.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.category.Category
import uz.texnopos.installment.databinding.FragmentProductBinding

class ProductFragment: Fragment(R.layout.fragment_product){

    private lateinit var binding: FragmentProductBinding
    private val categoryAdapter = CategoryAdapter()
    private val productAdapter = ProductAdapter()
    private var categoryId = 0
    private val viewModel: ProductViewModel by viewModel()
    private val categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObserver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBinding.bind(view).apply {
            rvCategory.adapter = categoryAdapter
        }
        viewModel.getCategories()
        categoryAdapter.setOnCategoryItemClickListener {
            toast("Basildi category item")
            setUpObserverProducts(it)
        }
    }

    private fun setUpObserver() {
        viewModel.getCategory.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> { }
                ResourceState.SUCCESS -> {
                    it.data!!.forEach { category ->
                        categoryList.add(category.category)
                        categoryAdapter.models = categoryList
                        categoryId = category.category.id
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

    private fun setUpObserverProducts(id: Int){
        viewModel.getCategory.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> { }
                ResourceState.SUCCESS -> {
                    it.data!!.forEach { category ->
                        if(id == category.category.id){
                            productAdapter.models = category.products
                        }
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