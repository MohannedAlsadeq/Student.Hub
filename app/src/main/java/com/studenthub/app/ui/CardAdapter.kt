package com.studenthub.app.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.studenthub.app.databinding.ItemCardBinding

/** What one row looks like. done == null hides the checkbox. */
data class CardItem(
    val id: Int,
    val title: String,
    val subtitle: String = "",
    val done: Boolean? = null,
    val deletable: Boolean = true
)

class CardAdapter(
    private val onClick: (Int) -> Unit,
    private val onToggle: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : ListAdapter<CardItem, CardAdapter.VH>(Diff) {

    class VH(val b: ItemCardBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.b) {
            title.text = item.title
            subtitle.text = item.subtitle
            subtitle.visibility = if (item.subtitle.isBlank()) View.GONE else View.VISIBLE
            title.paintFlags = if (item.done == true)
                title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            check.visibility = if (item.done == null) View.GONE else View.VISIBLE
            check.setOnCheckedChangeListener(null)
            check.isChecked = item.done == true
            check.setOnCheckedChangeListener { _, _ -> onToggle(item.id) }

            btnDelete.visibility = if (item.deletable) View.VISIBLE else View.GONE
            btnDelete.setOnClickListener { onDelete(item.id) }
            root.setOnClickListener { onClick(item.id) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<CardItem>() {
        override fun areItemsTheSame(a: CardItem, b: CardItem) = a.id == b.id
        override fun areContentsTheSame(a: CardItem, b: CardItem) = a == b
    }
}
