package uz.texnopos.installment.ui.main.product

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.R
import uz.texnopos.installment.core.inflate
import uz.texnopos.installment.core.onClick
import uz.texnopos.installment.data.model.category.Category
import uz.texnopos.installment.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(category: Category) {
            binding.apply {
                tvCategoryName.text = category.name
                tvCategoryName.onClick {
                    onCategoryItemClick.invoke(category.id)
                }
            }
        }
    }

    var models: MutableList<Category> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.bind(parent.inflate(R.layout.item_category)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    private var onCategoryItemClick: (id: Int) -> Unit = { }
    fun setOnCategoryItemClickListener(onCategoryItemClick: (id: Int) -> Unit) {
        this.onCategoryItemClick = onCategoryItemClick
    }

    override fun getItemCount(): Int = models.size
}