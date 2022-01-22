package uz.texnopos.installment.ui.main.product

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.texnopos.installment.R
import uz.texnopos.installment.core.inflate
import uz.texnopos.installment.data.model.category.Category
import uz.texnopos.installment.data.model.category.Product
import uz.texnopos.installment.databinding.ItemProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                Glide
                    .with(root.context)
                    .load(product.img_url)
                    .into(ivProduct)
            }
        }
    }

    var models: MutableList<Product> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.bind(parent.inflate(R.layout.item_product)))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    private var onCategoryItemClick: (category: Category) -> Unit = { }
    fun setOnProductItemClickListener(onCategoryItemClick: (category: Category) -> Unit) {
        this.onCategoryItemClick = onCategoryItemClick
    }

    override fun getItemCount(): Int = models.size
}