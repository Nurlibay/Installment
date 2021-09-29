package uz.texnopos.installment.ui.main.clients

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.ItemClientBinding

class ClientsAdapter : RecyclerView.Adapter<ClientsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(singleClient: Client) {
            binding.tvClientFullName.text = singleClient.client_name
            binding.tvProductCount.text = singleClient.count.toString()
            binding.tvPaidSum.text = "${singleClient.paid} / ${singleClient.all_sum}"
            binding.cardView.setOnClickListener {
                onItemClick.invoke(singleClient)
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

}