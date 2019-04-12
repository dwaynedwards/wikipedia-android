package com.newdaydevelopments.mikipedia.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.MikipediaApplication
import com.newdaydevelopments.mikipedia.adapters.PageListItemRecyclerAdapter
import com.newdaydevelopments.mikipedia.managers.MikipediaManager
import com.newdaydevelopments.mikipedia.models.PageModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class HistoryFragment : Fragment() {

    private val currentResults: ArrayList<PageModel> = ArrayList()
    private val adapter by lazy {
        PageListItemRecyclerAdapter(currentResults)
    }

    private lateinit var mikipediaManager: MikipediaManager
    private lateinit var historyPageRecycler: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mikipediaManager = (activity!!.applicationContext as MikipediaApplication).mikipediaManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        setHasOptionsMenu(true)

        historyPageRecycler = view.findViewById(R.id.recycler_history_page)
        historyPageRecycler.adapter = adapter

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
                        mikipediaManager.clearHistory()
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
        currentResults.addAll(mikipediaManager.getHistory())
        activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
    }
}
