package uz.texnopos.installment.ui.main.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.R
import uz.texnopos.installment.core.changeDateFormat2
import uz.texnopos.installment.core.changeFormat
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.databinding.ItemOrderBinding

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ClientOrdersViewHolder>() {

    inner class ClientOrdersViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(order: Order) {
            binding.apply {
                binding.price.text =
                    (order.productPrice.toInt() - order.firstPay).toString().changeFormat()
                binding.tvProductName.text = order.productName
                binding.tvDate.text =
                    "${order.startDate.changeDateFormat2()} / ${order.endDate.changeDateFormat2()}"
                Log.d("tek", order.startDate.split('-').toString())
                binding.tvOrderId.text = root.context.getString(R.string.order_id, order.orderId)
                binding.tvPaidSum.text =
                    root.context.getString(R.string.order_paid, order.paidSum.changeFormat())
                binding.imgCheck.setImageResource(
                    if (order.status == 1) R.drawable.ic_round_circle_green
                    else R.drawable.ic_round_circle_red
                )
                binding.cardView.setOnClickListener {
                    onItemCLick.invoke(order)
                }
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