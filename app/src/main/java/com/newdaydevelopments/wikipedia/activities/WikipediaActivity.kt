package com.newdaydevelopments.wikipedia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.newdaydevelopments.wikipedia.R
import com.newdaydevelopments.wikipedia.fragments.ExploreFragment
import com.newdaydevelopments.wikipedia.fragments.FavoritesFragment
import com.newdaydevelopments.wikipedia.fragments.HistoryFragment
import kotlinx.android.synthetic.main.activity_wikipedia.*

class WikipediaActivity : AppCompatActivity() {
    private val exploreFragment = ExploreFragment()
    private val favoritesFragment = FavoritesFragment()
    private val historyFragment = HistoryFragment()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment
        when (item.itemId) {
            R.id.navigation_favorites -> {
                fragment = favoritesFragment
            }
            R.id.navigation_history -> {
                fragment = historyFragment
            }
            else -> {
                fragment = exploreFragment
            }
        }

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .commit()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wikipedia)

        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, exploreFragment)
            .commit()
    }
}
