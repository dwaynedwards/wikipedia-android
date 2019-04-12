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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.WikiApplication
import com.newdaydevelopments.wikipedia.activities.SearchActivity
import com.newdaydevelopments.wikipedia.adapters.PageCardItemRecyclerAdapter
import com.newdaydevelopments.wikipedia.managers.WikiManager
import com.newdaydevelopments.wikipedia.models.WikiPageModel
import com.newdaydevelopments.wikipedia.models.WikiResultModel
import org.jetbrains.anko.doAsync

class ExploreFragment : Fragment() {

    companion object {
        private const val RESULT_KEY = "result"
    }

    private val currentResults: ArrayList<WikiPageModel> = ArrayList()
    private val adapter by lazy {
        PageCardItemRecyclerAdapter(currentResults)
    }

    private lateinit var wikiManager: WikiManager

    private lateinit var searchCard: CardView
    private lateinit var exploreArticleRecycler: RecyclerView
    private lateinit var refresher: SwipeRefreshLayout

    private var resultJson: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    private var exploreView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exploreView = inflater.inflate(R.layout.fragment_explore, container, false)

        refresher = exploreView!!.findViewById(R.id.refresher)
        refresher.setOnRefreshListener {
            doAsync {
                getRandomArticles()
            }
        }

        searchCard = exploreView!!.findViewById(R.id.card_search)
        searchCard.setOnClickListener {
            val searchIntent = Intent(context, SearchActivity::class.java)
            context!!.startActivity(searchIntent)
        }

        exploreArticleRecycler = exploreView!!.findViewById(R.id.recycler_explore_article)
        exploreArticleRecycler.adapter = adapter

        return exploreView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(RESULT_KEY, resultJson)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            resultJson = savedInstanceState.getString(RESULT_KEY, "")
            if (resultJson != "") {
                println("Has State!!!!!!!!!!")
                doAsync {
                    refresher.isRefreshing = true
                    val result = Gson().fromJson(resultJson, WikiResultModel::class.java)
                    currentResults.clear()
                    currentResults.addAll(result.query!!.pages)
                    activity!!.runOnUiThread {
                        adapter.notifyDataSetChanged()
                        refresher.isRefreshing = false
                    }
                }
            }

        }
    }

    private fun getRandomArticles() {
        refresher.isRefreshing = true
        try {
            wikiManager.getRandom(20) { result ->
                resultJson = Gson().toJson(result)
                currentResults.clear()
                currentResults.addAll(result.query!!.pages)
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
