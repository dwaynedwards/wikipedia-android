package com.newdaydevelopments.mikipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.holders.ItemViewHolder
import com.newdaydevelopments.mikipedia.models.PageModel

class PageListItemRecyclerAdapter(private val currentResults: ArrayList<PageModel>):
        RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val listItem = LayoutInflater.from(parent.context)
                                            .inflate(R.layout.page_list_item, parent, false)
        return ItemViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.updateWithPage(currentResults[position])
    }
}