package com.newdaydevelopments.mikipedia.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.MikipediaApplication
import com.newdaydevelopments.mikipedia.managers.MikipediaManager
import com.newdaydevelopments.mikipedia.models.PageModel
import kotlinx.android.synthetic.main.activity_article_detail.*
import org.jetbrains.anko.toast

class PageDetailActivity: AppCompatActivity() {

    companion object {
        const val WIKI_PAGE_KEY = "page"
    }

    private lateinit var mikipediaManager: MikipediaManager
    private lateinit var currentPage: PageModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mikipediaManager = (applicationContext as MikipediaApplication).mikipediaManager

        val wikiPageJson = intent.getStringExtra(WIKI_PAGE_KEY)
        currentPage = Gson().fromJson<PageModel>(wikiPageJson, PageModel::class.java)

        supportActionBar!!.title = currentPage.title

        webview_article_detail.webViewClient = WebViewClient()
        webview_article_detail.loadUrl(currentPage.fullurl)

        if (!mikipediaManager.hasHistory(currentPage.pageid)) {
            mikipediaManager.addHistory(currentPage)
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
                if (mikipediaManager.isFavorite(currentPage.pageid)) {
                    mikipediaManager.removeFavorite(currentPage.pageid)
                    toast("Page removed from favorites")
                } else {
                    mikipediaManager.addFavorite(currentPage)
                    toast("Page added to favorites")
                }
            } catch (ex: Exception) {
                toast("Unable to update this page: ${ex.message}")
            }
        }
        return true
    }
}