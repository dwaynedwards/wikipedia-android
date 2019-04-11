package com.newdaydevelopments.wikipedia.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.WikiApplication
import com.newdaydevelopments.wikipedia.activities.SearchActivity
import com.newdaydevelopments.wikipedia.adapters.PageCardItemRecyclerAdapter
import com.newdaydevelopments.wikipedia.managers.WikiManager
import kotlin.Exception

class ExploreFragment : Fragment() {

    private val adapter: PageCardItemRecyclerAdapter = PageCardItemRecyclerAdapter()

    private lateinit var wikiManager: WikiManager

    private lateinit var searchCard: CardView
    private lateinit var exploreArticleRecycler: RecyclerView
    private lateinit var refresher: SwipeRefreshLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)

        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        searchCard = view.findViewById(R.id.card_search)
        searchCard.setOnClickListener {
            val searchIntent = Intent(context, SearchActivity::class.java)
            context!!.startActivity(searchIntent)
        }

        exploreArticleRecycler = view.findViewById(R.id.recycler_explore_article)
        exploreArticleRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        exploreArticleRecycler.adapter = adapter

        refresher = view.findViewById(R.id.refresher)
        refresher.setOnRefreshListener {
            getRandomArticles()
        }
        return view
    }

    private fun getRandomArticles() {
        refresher.isRefreshing = true
        try {
            wikiManager.getRandom(20) { result ->
                adapter.currentResults.clear()
                adapter.currentResults.addAll(result.query!!.pages)
                activity!!.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    refresher.isRefreshing = false
                }
            }
        } catch (ex: Exception) {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Refresh error!").setMessage(ex.message).create().show()
        }
    }
}
