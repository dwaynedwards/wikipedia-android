package com.newdaydevelopments.mikipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.holders.ItemViewHolder
import com.newdaydevelopments.mikipedia.models.PageModel

class PageCardItemRecyclerAdapter(private val currentResults: ArrayList<PageModel>):
        RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val cardItem = LayoutInflater.from(parent.context)
                                            .inflate(R.layout.page_card_item, parent, false)
        return ItemViewHolder(cardItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.updateWithPage(currentResults[position])
    }
}