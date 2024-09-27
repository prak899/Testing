package com.example.testing.kotlin_activity

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R

class ListAdapter(val itemList: MutableList<String>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    val selectedItems = mutableListOf<Int>()
    private var selectionMode = false

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val itemName: TextView = itemView.findViewById(R.id.itemName)
//        val checkMark: ImageView = itemView.findViewById(R.id.checkMark)
        val cardView: CardView = itemView.findViewById(R.id.card_view)

        val text1: TextView = itemView.findViewById(R.id.textView1)
        val text2: TextView = itemView.findViewById(R.id.textView2)
        val text3: TextView = itemView.findViewById(R.id.textView3)
        val text4: TextView = itemView.findViewById(R.id.textView4)
        val text5: TextView = itemView.findViewById(R.id.textView5)
        val text6: TextView = itemView.findViewById(R.id.textView6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crm_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text1.text = "${itemList[position]} : ${itemList[position]}"
        holder.text2.text = "${itemList[position]} : ${itemList[position]}"
        holder.text3.text = "${itemList[position]} : ${itemList[position]}"
        holder.text4.text = "${itemList[position]} : ${itemList[position]}"
        holder.text5.text = "${itemList[position]} : ${itemList[position]}"
        holder.text6.text = "${itemList[position]} : ${itemList[position]}"

//        holder.checkMark.visibility =
//            if (selectedItems.contains(position)) View.VISIBLE else View.GONE

        holder.cardView.setCardBackgroundColor(
            if (selectedItems.contains(position)) {
                Color.GRAY
            } else {
                Color.WHITE
            }
        )
        holder.itemView.setOnLongClickListener {
            if (!selectionMode) {
                selectionMode = true
                toggleSelection(position)
                logSelectedItems(holder)
                onSelectionChangedListener?.invoke(true)
            }
            true
        }

        holder.itemView.setOnClickListener {
            if (selectionMode) {
                toggleSelection(position)
                logSelectedItems(holder)
                onSelectionChangedListener?.invoke(selectedItems.isNotEmpty())
            }
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int = itemList.size

    var onSelectionChangedListener: ((Boolean) -> Unit)? = null

    fun clearSelection() {
        selectionMode = false
        selectedItems.clear()
        notifyDataSetChanged()
    }

    private fun logSelectedItems(holder: MyViewHolder) {
        val selectedNames = selectedItems.map { itemList[it] }
        Log.d("SelectedItems", "Selected items: $selectedNames")
    }

}
