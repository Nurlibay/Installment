package uz.texnopos.installment.ui.main.orders

import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.isDigitsOnly
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
                order.apply {
                    if (!productPrice.isDigitsOnly())    productPrice="0"
                    price.text = (productPrice.toLong() - firstPay).changeFormat()
                    tvProductName.text = productName
                    tvDate.text =fromHtml(startDate,endDate)
                    tvOrderId.text = root.context.getString(R.string.order_id, orderId)
                    tvPaidSum.text = root.context.getString(R.string.order_paid, paidSum.changeFormat())
                    imgCheck.setImageResource(
                        if (status == 1) R.drawable.ic_round_circle_green
                        else R.drawable.ic_round_circle_red
                    )
                    binding.cardView.setOnClickListener {
                        onItemCLick.invoke(order)
                    }
                }
            }
        }
    }

    private var models = mutableListOf<Order>()
    fun setData(models: List<Order>) {
        this.models.clear()
        for (model in models) {
            this.models.add(0, model)
            notifyItemChanged(0)
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
    fun fromHtml(start:String,end:String):Spanned{
        val b="<i>от</i> <b>${start.changeDateFormat2()}</b> <i>до</i> <b>${end.changeDateFormat2()}</b>"
        return HtmlCompat.fromHtml(b,HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}