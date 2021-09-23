package uz.texnopos.installment.core

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var models: List<T> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun onAdded(data: T) {
        val list = models.toMutableList()
        list.add(data)
        models = list
        notifyItemInserted(list.lastIndex)
    }
    fun onRemoved(data: T) {
        val index = models.indexOf(data)
        val list = models.toMutableList()
        list.removeAt(index)
        models = list
        notifyItemRemoved(index)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun update() {
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = models.size

    fun textFormat(t: String): String{
        var text = t
        text = text.reversed()
        text = text.subSequence(0, text.length)
            .chunked(3)
            .joinToString(" ")
        return text.reversed()
    }
    @SuppressLint("SimpleDateFormat")
    fun convertLongString(l: Long): String{
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(l))
    }
}