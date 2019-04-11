package com.newdaydevelopments.wikipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.holders.PageCardItemHolder
import com.newdaydevelopments.wikipedia.models.WikiPageModel

class PageCardItemRecyclerAdapter: RecyclerView.Adapter<PageCardItemHolder>() {

    val currentResults: ArrayList<WikiPageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageCardItemHolder {
        val cardItem = LayoutInflater.from(parent.context).inflate(R.layout.page_card_item, parent, false)
        return PageCardItemHolder(cardItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: PageCardItemHolder, position: Int) {
        val page = currentResults[position]
        holder.updateWithPage(page)
    }
}