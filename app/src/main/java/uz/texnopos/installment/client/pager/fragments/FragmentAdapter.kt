package uz.texnopos.installment.client.pager.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.data.ModelPhoneList
import uz.texnopos.installment.databinding.ItemPhoneListBinding

class FragmentAdapter: RecyclerView.Adapter<FragmentAdapter.FragmentViewHolder>() {

    inner class FragmentViewHolder(private val binding: ItemPhoneListBinding):
        RecyclerView.ViewHolder(binding.root){
            fun populateModel(model: ModelPhoneList){
                binding.tvPhoneName.text = model.phoneName
                binding.tvData.text = model.data
                binding.price.text = model.price
            }
    }

    var models :MutableList<ModelPhoneList> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder {
        val binding = ItemPhoneListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FragmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

}