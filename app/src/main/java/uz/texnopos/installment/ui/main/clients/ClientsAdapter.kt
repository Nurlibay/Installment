package uz.texnopos.installment.ui.main.clients

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.R
import uz.texnopos.installment.core.changeFormat
import uz.texnopos.installment.core.contains2
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
            //binding.tvClientIdValue.text = client.client_id.toString()
            binding.tvPaidSum.text = client.paid.toInt().toString().changeFormat()
            binding.tvAllSum.text = client.all_sum.toInt().toString().changeFormat()
            binding.cardView.onClick {
                onItemClick.invoke(client)
            }
            if(client.color == "green"){
                binding.imgCheck.setImageResource(R.drawable.ic_round_circle_green)
            }
            if(client.color == "red"){
                binding.imgCheck.setImageResource(R.drawable.ic_round_circle_red)
            }
            if(client.color == "yellow"){
                binding.imgCheck.setImageResource(R.drawable.ic_round_circle_yellow)
            }
        }
    }

    var models : MutableList<Client> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            models.clear()
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

    fun filterClientNameAndClientId(s: String, clients: List<Client>): Boolean {
        val filteredList : MutableList<Client> = mutableListOf()
        for (client in clients) {
            if (client.client_name.contains2(s) ||
                client.client_id.toString().contains2(s)
            ) {
                filteredList.add(client)
            }
        }
        models = filteredList
        return filteredList.isNotEmpty()
    }
}