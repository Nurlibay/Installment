package uz.texnopos.installment.ui.main.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.data.model.Transaction
import uz.texnopos.installment.databinding.ItemTransactionBinding


class ClientTransactionsAdapter : RecyclerView.Adapter<ClientTransactionsAdapter.ClientTransactionsViewHolder>()  {

    inner class ClientTransactionsViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun populateModel(clientTransaction: Transaction) {
                binding.tvDate.text=clientTransaction.pay_date
                binding.tvQuantity.text=clientTransaction.paid
                binding.cardView.setOnClickListener{
                    onItemCLick.invoke()
                }
            }
    }

    var models = mutableListOf<Transaction>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemCLick: () -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientTransactionsViewHolder {
        val itemView = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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