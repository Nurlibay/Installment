package uz.texnopos.installment.ui.main.client.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.databinding.ItemPhoneListBinding

class ClientOrdersAdapter : RecyclerView.Adapter<ClientOrdersAdapter.ClientOrdersViewHolder>() {

    inner class ClientOrdersViewHolder(private val binding: ItemPhoneListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun populateModel(order: Int) {
            // binding.tvProductName.text = order.product_name
            // binding.tvData.text = "${order.start_date} - ${order.end_date}"

            binding.cardView.setOnClickListener {
                onItemCLick.invoke()
            }
        }
    }

    var models = mutableListOf(1,2,3,4,5,6)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemCLick: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientOrdersViewHolder {
        val itemView = ItemPhoneListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ClientOrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientOrdersViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

    fun onItemClick(onItemClick: () -> Unit) {
        this.onItemCLick = onItemClick
    }
}