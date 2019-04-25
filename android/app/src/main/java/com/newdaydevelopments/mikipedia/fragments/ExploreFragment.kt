package com.newdaydevelopments.mikipedia.fragments


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
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.MikipediaApplication
import com.newdaydevelopments.mikipedia.activities.SearchActivity
import com.newdaydevelopments.mikipedia.adapters.PageCardItemRecyclerAdapter
import com.newdaydevelopments.mikipedia.managers.MikipediaManager
import com.newdaydevelopments.mikipedia.models.PageModel
import com.newdaydevelopments.mikipedia.models.ResultModel
import org.jetbrains.anko.doAsync

class ExploreFragment : Fragment() {

    companion object {
        private const val RESULT_KEY = "result"
    }

    private val currentResults: ArrayList<PageModel> = ArrayList()
    private val adapter by lazy {
        PageCardItemRecyclerAdapter(currentResults)
    }

    private lateinit var mikipediaManager: MikipediaManager

    private lateinit var searchCard: CardView
    private lateinit var explorePageRecycler: RecyclerView
    private lateinit var refresher: SwipeRefreshLayout

    private var resultJson: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mikipediaManager = (activity!!.applicationContext as MikipediaApplication).mikipediaManager
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

        explorePageRecycler = exploreView!!.findViewById(R.id.recycler_explore_page)
        explorePageRecycler.adapter = adapter

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
                    val result = Gson().fromJson(resultJson, ResultModel::class.java)
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
            mikipediaManager.getRandom(20) { result ->
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
