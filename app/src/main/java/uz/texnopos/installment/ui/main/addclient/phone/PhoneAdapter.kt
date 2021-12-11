package uz.texnopos.installment.ui.main.addclient.phone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.installment.core.mask.MaskWatcherPhone
import uz.texnopos.installment.core.textToString
import uz.texnopos.installment.data.model.Phone
import uz.texnopos.installment.databinding.ItemPhoneBinding

class PhoneAdapter : RecyclerView.Adapter<PhoneAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val bind: ItemPhoneBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun populateModel(phone: Phone, position: Int) {
            bind.apply {
                etPhone.addTextChangedListener(MaskWatcherPhone.phoneNumber())
                etPhone.setText(phone.phone)
                inputPhone.setEndIconOnClickListener {
                    if (etPhone.hasFocus()||inputPhone.hasFocus()) {
                        inputPhone.clearFocus()
                        etPhone.clearFocus()
                    }
                    else {
                        if (etPhone.textToString().isEmpty()) {
                            if (models.size > 1) remove(models.indexOf(phone))
                        } else etPhone.text?.clear()
                    }
                }
            }
        }
    }

    private var models = mutableListOf<Phone>()

    private var removeClick: () -> Unit = {}
    fun onRemoveClick(removeClick: () -> Unit) {
        this.removeClick = removeClick
    }

    fun add() {
        if (models.size < 10) {
            models.add(Phone())
            notifyItemInserted(models.lastIndex)
        }
    }

    fun remove(position: Int) {
        models.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = models.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val bind = ItemPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.populateModel(models[position], position)
    }
}