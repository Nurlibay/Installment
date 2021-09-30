package uz.texnopos.installment.ui.main.clients

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.core.onClick
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.ItemClientBinding

class ClientsAdapter : RecyclerView.Adapter<ClientsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(client: Client) {
            binding.tvClientFullName.text = client.client_name
            binding.tvProductCount.text = client.count.toString()
            binding.tvPaidSum.text = client.paid.toString()
            binding.tvAllSum.text = client.all_sum.toString()
            binding.cardView.onClick {
                onItemClick.invoke(client)
            }
        }
    }
    var models : MutableList<Client> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClick: (singleClient: Client) -> Unit = {}
    fun onItemClick(onItemClick: (singleClient: Client) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun getItemCount() = models.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filteredList(clientsItem: MutableList<Client>) {
        models = clientsItem
        notifyDataSetChanged()
    }
}