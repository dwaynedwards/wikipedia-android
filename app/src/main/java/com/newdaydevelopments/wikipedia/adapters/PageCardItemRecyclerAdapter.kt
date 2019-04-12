package com.newdaydevelopments.wikipedia.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.activities.PageDetailActivity
import com.newdaydevelopments.wikipedia.models.WikiPageModel
import com.squareup.picasso.Picasso

class PageCardItemRecyclerAdapter(private val currentResults: ArrayList<WikiPageModel>):
        RecyclerView.Adapter<PageCardItemRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardItem = LayoutInflater.from(parent.context)
                                            .inflate(R.layout.page_card_item, parent, false)
        return ViewHolder(cardItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.updateWithPage(currentResults[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val articleImage = itemView.findViewById<ImageView>(R.id.image_article)
        private val articleTitleText = itemView.findViewById<TextView>(R.id.text_article_title)

        private var currentPage: WikiPageModel? = null

        init {
            itemView.setOnClickListener {
                val detailPageIntent = Intent(itemView.context, PageDetailActivity::class.java)
                detailPageIntent.putExtra(PageDetailActivity.WIKI_PAGE_KEY, Gson().toJson(currentPage))
                itemView.context.startActivity(detailPageIntent)
            }
        }

        fun updateWithPage(page: WikiPageModel) {
            if (page.thumbnail != null) {
                Picasso.get()
                    .load(page.thumbnail!!.source)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_24)
                    .resize(200, 200)
                    .into(articleImage)
            } else {
                articleImage.setImageResource(R.drawable.baseline_image_24)
            }
            articleTitleText.text = page.title
            currentPage = page
        }
    }
}