package uz.texnopos.installment.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.databinding.ItemClientBinding

class ClientAdapter : RecyclerView.Adapter<ClientAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val bind:ItemClientBinding) : RecyclerView.ViewHolder(bind.root) {
        fun populateModel(model:Int) {

        }
    }
    var models= mutableListOf(1,2,3,4,5,6,7,8,9,10)
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    override fun getItemCount()=models.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val bind= ItemClientBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(bind)
    }
     override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}