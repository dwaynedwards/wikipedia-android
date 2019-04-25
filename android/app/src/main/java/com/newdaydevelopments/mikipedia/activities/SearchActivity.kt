package com.newdaydevelopments.mikipedia.activities

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.MikipediaApplication
import com.newdaydevelopments.mikipedia.adapters.PageListItemRecyclerAdapter
import com.newdaydevelopments.mikipedia.managers.MikipediaManager
import com.newdaydevelopments.mikipedia.models.PageModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private val currentResults: ArrayList<PageModel> = ArrayList()
    private val adapter by lazy {
        PageListItemRecyclerAdapter(currentResults)
    }

    private lateinit var mikipediaManager: MikipediaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        mikipediaManager = (applicationContext as MikipediaApplication).mikipediaManager

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
                mikipediaManager.search(query!!, 20, 0) { result ->
                    val size = currentResults.size
                    if (size != 0) {
                        currentResults.clear()
                    }
                    if (result.query != null) {
                        currentResults.addAll(result.query!!.pages)
                    }
                    if (currentResults.size != size) {
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
