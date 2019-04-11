package com.newdaydevelopments.wikipedia.activities

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.WikiApplication
import com.newdaydevelopments.wikipedia.adapters.PageListItemRecyclerAdapter
import com.newdaydevelopments.wikipedia.managers.WikiManager
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var wikiManager: WikiManager

    private val adapter: PageListItemRecyclerAdapter = PageListItemRecyclerAdapter()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        wikiManager = (applicationContext as WikiApplication).wikiManager

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recycler_search_results.layoutManager = LinearLayoutManager(this)
        recycler_search_results.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_menu_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem!!.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        searchView.requestFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                wikiManager.search(query!!, 20, 0) { result ->
                    val size = adapter.currentResults.size
                    if (size != 0) {
                        adapter.currentResults.clear()
                    }
                    if (result.query != null) {
                        adapter.currentResults.addAll(result.query!!.pages)
                    }
                    if (adapter.currentResults.size != size) {
                        runOnUiThread { adapter.notifyDataSetChanged() }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}
