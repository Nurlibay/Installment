package uz.texnopos.installment.ui.main.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.data.model.SingleClient
import uz.texnopos.installment.databinding.ItemClientBinding

class ClientsAdapter : RecyclerView.Adapter<ClientsAdapter.ItemViewHolder>() {
<<<<<<< HEAD

    inner class ItemViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(singleClient: SingleClient) {
            binding.tvClientFullName.text = singleClient.client_name
            binding.tvProductCount.text = singleClient.count.toString()
            binding.tvPaidSum.text = "${singleClient.paid} / ${singleClient.all_sum}"
            binding.cardView.setOnClickListener {
                onItemClick.invoke(singleClient)
            }
        }
    }

    var models : MutableList<SingleClient> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
=======
    inner class ItemViewHolder(private val bind:ItemClientBinding) : RecyclerView.ViewHolder(bind.root) {
        fun populateModel(model:Int) {
        bind.cardView.setOnClickListener {
            onItemClick.invoke()
        }
        }
    }
    var models = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
        set(value) {
            field = value
            notifyDataSetChanged()
        }
<<<<<<< HEAD

    private var onItemClick: (singleClient: SingleClient) -> Unit = {}
    fun onItemClick(onItemClick: (singleClient: SingleClient) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun getItemCount() = models.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
=======
    private var onItemClick: () -> Unit = {}
    override fun getItemCount()= models.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val bind= ItemClientBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(bind)
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
<<<<<<< HEAD
=======

    fun onItemClick(onItemClick: () -> Unit) {
        this.onItemClick = onItemClick
    }
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
}