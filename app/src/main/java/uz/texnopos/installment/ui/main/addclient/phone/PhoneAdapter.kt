package uz.texnopos.installment.ui.main.addclient.phone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import uz.texnopos.installment.core.getOnlyDigits
import uz.texnopos.installment.databinding.ItemPhoneBinding


class PhoneAdapter : RecyclerView.Adapter<PhoneAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val bind: ItemPhoneBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun populateModel(phone: String, position: Int) {
            bind.apply {
                etPhone.setText(phone)
                etPhone.doOnTextChanged { text, _, _, _ ->
                    models[position] = text.toString()
                }
                etPhone.addMaskAndHint("([00]) [000]-[00]-[00]")
            }
        }

        private fun TextInputEditText.addMaskAndHint(mask: String) {
            val listener = installOn(
                this,
                mask
            )
            this.hint = listener.placeholder()
        }
    }

    private var models = mutableListOf<String>()

    private var removeClick: () -> Unit = {}
    fun onRemoveClick(removeClick: () -> Unit) {
        this.removeClick = removeClick
    }

    fun add() {
        if (models.size < 10) {
            models.add("")
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

    fun getAllPhones():List<String>{
        return models.map { it.getOnlyDigits() }.toMutableList().fillList(10)
    }
    private fun MutableList<String>.fillList(maxSize: Int, s: String=""): List<String> {
        val sz = this.size
        repeat(maxSize - sz) {
            this.add(s)
        }
        return this
    }

}