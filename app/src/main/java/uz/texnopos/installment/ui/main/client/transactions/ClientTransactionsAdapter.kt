package uz.texnopos.installment.ui.main.client.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.databinding.ItemClientTransactionBinding


class ClientTransactionsAdapter : RecyclerView.Adapter<ClientTransactionsAdapter.ClientTransactionsViewHolder>()  {

    inner class ClientTransactionsViewHolder(private val binding: ItemClientTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun populateModel(clientTransaction: Int) {

                binding.cardView.setOnClickListener{
                    onItemCLick.invoke()
                }
            }
    }

    var models = mutableListOf(1,2,3,4,5,6,7,8)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemCLick: () -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientTransactionsViewHolder {
        val itemView = ItemClientTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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