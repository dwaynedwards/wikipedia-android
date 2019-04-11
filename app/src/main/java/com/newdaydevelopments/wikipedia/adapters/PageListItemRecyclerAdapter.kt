package com.newdaydevelopments.wikipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.holders.PageListItemHolder
import com.newdaydevelopments.wikipedia.models.WikiPageModel

class PageListItemRecyclerAdapter: RecyclerView.Adapter<PageListItemHolder>() {

    val currentResults: ArrayList<WikiPageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageListItemHolder {
        val listItem = LayoutInflater.from(parent.context).inflate(R.layout.page_list_item, parent, false)
        return PageListItemHolder(listItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: PageListItemHolder, position: Int) {
        val page = currentResults[position]
        holder.updateWithPage(page)
    }
}