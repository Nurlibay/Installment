package uz.texnopos.installment.ui.main.orders.call

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.core.onClick
import uz.texnopos.installment.data.model.Phone
import uz.texnopos.installment.databinding.PhoneItemBinding

class CallAdapter : RecyclerView.Adapter<CallAdapter.CallViewHolder>() {

    inner class CallViewHolder(private val binding: PhoneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(phone: Phone) {
            binding.apply {
                phoneNumber.text = phone.phone
                phoneItem.onClick {
                    onItemCLick.invoke(phone)
                }
            }
        }
    }

    var models = mutableListOf<Phone>()
    fun setData(models: List<Phone>) {
        this.models.clear()
        for (model in models) {
            this.models.add(0, model)
            notifyItemChanged(0)
        }
    }

    private var onItemCLick: (Phone) -> Unit = {}
    fun onItemClick(onItemClick: (Phone) -> Unit) {
        this.onItemCLick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val itemView = PhoneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }
}