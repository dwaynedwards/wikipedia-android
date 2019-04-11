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
import com.newdaydevelopments.wikipedia.managers.WikiManager
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class FavoritesFragment : Fragment() {

    private lateinit var wikiManager: WikiManager
    private lateinit var favoritesArticleRecycler: RecyclerView

    private val adapter = PageCardItemRecyclerAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        wikiManager = (activity!!.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        setHasOptionsMenu(true)

        favoritesArticleRecycler = view.findViewById(R.id.recycler_favorites_article)
        favoritesArticleRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        favoritesArticleRecycler.adapter = adapter

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
            activity!!.alert("Are you sure you want to clear your favorites?", "Confirm") {
                yesButton {
                    doAsync {
                        wikiManager.clearFavorites()
                        refreshRecyclerView()
                    }
                }
                noButton {}
            }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshRecyclerView() {
        val favorites = wikiManager.getFavorites()
        adapter.currentResults.clear()
        adapter.currentResults.addAll(favorites)
        activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
    }
}
