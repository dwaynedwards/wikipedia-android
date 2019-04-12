package com.newdaydevelopments.mikipedia.holders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.activities.PageDetailActivity
import com.newdaydevelopments.mikipedia.models.PageModel
import com.squareup.picasso.Picasso

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val pageImage = itemView.findViewById<ImageView>(R.id.image_page)
    private val pageTitleText = itemView.findViewById<TextView>(R.id.text_article_page)

    private lateinit var currentPage: PageModel

    init {
        itemView.setOnClickListener {
            val detailPageIntent = Intent(itemView.context, PageDetailActivity::class.java)
            detailPageIntent.putExtra(PageDetailActivity.WIKI_PAGE_KEY, Gson().toJson(currentPage))
            itemView.context.startActivity(detailPageIntent)
        }
    }

    fun updateWithPage(page: PageModel) {
        if (page.thumbnail != null) {
            Picasso.get()
                .load(page.thumbnail!!.source)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_image_24)
                .resize(200, 200)
                .into(pageImage)
        } else {
            pageImage.setImageResource(R.drawable.baseline_image_24)
        }
        pageTitleText.text = page.title
        currentPage = page
    }
}