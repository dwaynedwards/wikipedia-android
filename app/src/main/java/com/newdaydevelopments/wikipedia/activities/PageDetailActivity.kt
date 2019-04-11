package com.newdaydevelopments.wikipedia.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.WikiApplication
import com.newdaydevelopments.wikipedia.managers.WikiManager
import com.newdaydevelopments.wikipedia.models.WikiPageModel
import kotlinx.android.synthetic.main.activity_article_detail.*
import org.jetbrains.anko.toast

class PageDetailActivity: AppCompatActivity() {

    companion object {
        const val WIKI_PAGE_KEY = "page"
    }

    private lateinit var wikiManager: WikiManager
    private lateinit var currentPage: WikiPageModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        wikiManager = (applicationContext as WikiApplication).wikiManager

        val wikiPageJson = intent.getStringExtra(WIKI_PAGE_KEY)
        currentPage = Gson().fromJson<WikiPageModel>(wikiPageJson, WikiPageModel::class.java)

        supportActionBar!!.title = currentPage.title

        webview_article_detail.webViewClient = WebViewClient()
        webview_article_detail.loadUrl(currentPage.fullurl)

        if (!wikiManager.hasHistory(currentPage.pageid)) {
            wikiManager.addHistory(currentPage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_menu_favorite) {
            try {
                if (wikiManager.isFavorite(currentPage.pageid)) {
                    wikiManager.removeFavorite(currentPage.pageid)
                    toast("Page removed from favorites")
                } else {
                    wikiManager.addFavorite(currentPage)
                    toast("Page added to favorites")
                }
            } catch (ex: Exception) {
                toast("Unable to update this page: ${ex.message}")
            }
        }
        return true
    }
}