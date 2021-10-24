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
            binding.apply {
                client.apply {
                    tvClientFullName.text = clientName
                    tvProductCount.text = count.toString()
                    tvClientId.text = "Клиент номер: $clientId"
                    tvPaidSum.text = paid.toInt().toString().changeFormat()
                    tvAllSum.text = allSum.toInt().toString().changeFormat()
                    cardView.onClick {
                        onItemClick.invoke(client)
                    }
                    if (color == "green") imgCheck.setImageResource(R.drawable.ic_round_circle_green)
                    if (color == "red") imgCheck.setImageResource(R.drawable.ic_round_circle_red)
                    if (color == "yellow") imgCheck.setImageResource(R.drawable.ic_round_circle_yellow)
                }
            }
        }
    }

    private var models : MutableList<Client> = mutableListOf()
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
            if (client.clientName.contains2(s) ||
                client.clientId.toString().contains2(s)
            ) {
                filteredList.add(client)
            }
        }
        models = filteredList
        return filteredList.isNotEmpty()
    }
}