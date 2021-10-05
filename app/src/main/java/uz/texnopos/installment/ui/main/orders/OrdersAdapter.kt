package uz.texnopos.installment.ui.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.core.changeFormat
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.databinding.ItemOrderBinding

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ClientOrdersViewHolder>() {

    inner class ClientOrdersViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(order: Order) {
            binding.price.text = (order.product_price.toInt() - order.first_pay).toString().changeFormat()
            binding.tvProductName.text = order.product_name
            binding.tvData.text = "${order.start_date} - ${order.end_date}"
            binding.cardView.setOnClickListener {
                onItemCLick.invoke(order)
            }
        }
    }

    private var models = mutableListOf<Order>()
    fun setData(models: List<Order>) {
        this.models.clear()
        for (model in models) {
            this.models.add(model)
            notifyItemChanged(this.models.lastIndex)
        }
    }

    private var onItemCLick: (Order) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientOrdersViewHolder {
        val itemView = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientOrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientOrdersViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

    fun onItemClick(onItemClick: (Order) -> Unit) {
        this.onItemCLick = onItemClick
    }
}