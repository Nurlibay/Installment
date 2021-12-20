package uz.texnopos.installment.core.mask

import android.text.Editable
import android.text.TextWatcher

class MaskWatcherPhone(private val mask: String) : TextWatcher {
    private var isRunning = false
    private var isDeleting = false
    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable) {
        if (isRunning || isDeleting) {
            return
        }
        isRunning = true
        val editableLength = editable.length
        if (editableLength < mask.length) {
            if (mask[editableLength] != '#') {
                editable.append(mask[editableLength])
            } else if (mask[editableLength] != '#') {
                editable.insert(editableLength , mask, editableLength , editableLength)
            }
        }
        isRunning = false
    }

    companion object {
        fun phoneNumber(): MaskWatcherPhone {
            return MaskWatcherPhone("## ### ## ##")
        }
    }
}