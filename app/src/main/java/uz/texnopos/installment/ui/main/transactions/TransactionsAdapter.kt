package uz.texnopos.installment.ui.main.transactions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.core.changeDateFormat
import uz.texnopos.installment.core.changeFormat
import uz.texnopos.installment.core.onClick
import uz.texnopos.installment.data.model.Transactions
import uz.texnopos.installment.databinding.ItemTransactionBinding

class TransactionsAdapter :
    RecyclerView.Adapter<TransactionsAdapter.ClientTransactionsViewHolder>() {

    inner class ClientTransactionsViewHolder(private val bind: ItemTransactionBinding) :
        RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(transaction: Transactions.Transaction) {
            bind.apply {
                transaction.apply {
                    tvDate.text = payDate.changeDateFormat()
                    tvQuantity.text = "+"+paid.changeFormat()
                }
                cardView.onClick {
                    onItemCLick.invoke()
                }
            }
        }
    }

     var models = mutableListOf<Transactions.Transaction>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            value.sortByDescending { it.createdAt }
            field = value
            notifyDataSetChanged()
        }
    private var onItemCLick: () -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientTransactionsViewHolder {
        val itemView =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientTransactionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientTransactionsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

    fun onItemClick(onItemClick: () -> Unit) {
        this.onItemCLick = onItemClick
    }
}