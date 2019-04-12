package com.newdaydevelopments.wikipedia.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.WikiApplication
import com.newdaydevelopments.wikipedia.adapters.PageCardItemRecyclerAdapter
import com.newdaydevelopments.wikipedia.adapters.PageListItemRecyclerAdapter
import com.newdaydevelopments.wikipedia.managers.WikiManager
import com.newdaydevelopments.wikipedia.models.WikiPageModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class HistoryFragment : Fragment() {

    private val currentResults: ArrayList<WikiPageModel> = ArrayList()
    private val adapter by lazy {
        PageListItemRecyclerAdapter(currentResults)
    }

    private lateinit var wikiManager: WikiManager
    private lateinit var historyArticleRecycler: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        setHasOptionsMenu(true)

        historyArticleRecycler = view.findViewById(R.id.recycler_history_article)
        historyArticleRecycler.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            refreshRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_clear, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_clear) {
            activity!!.alert("Are you sure you want to clear your history?", "Confirm") {
                yesButton {
                    doAsync {
                        wikiManager.clearHistory()
                        refreshRecyclerView()
                    }
                }
                noButton {}
            }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshRecyclerView() {
        currentResults.clear()
        currentResults.addAll(wikiManager.getHistory())
        activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
    }
}
