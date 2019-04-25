package com.newdaydevelopments.mikipedia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.newdaydevelopments.mikipedia.R
import com.newdaydevelopments.mikipedia.fragments.ExploreFragment
import com.newdaydevelopments.mikipedia.fragments.FavoritesFragment
import com.newdaydevelopments.mikipedia.fragments.FragmentStateHelper
import com.newdaydevelopments.mikipedia.fragments.HistoryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val STATE_HELPER_KEY = "helper"
    }

    private val fragments = mutableMapOf<Int, Fragment>()

    private lateinit var stateHelper: FragmentStateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        stateHelper = FragmentStateHelper(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener { item ->
            val fragment =
                fragments[item.itemId] ?: getFragmentById(item.itemId)
            fragments[item.itemId] = fragment

            if (navigation.selectedItemId != 0) {
                saveCurrentState()
                stateHelper.restoreState(fragment, item.itemId)
            }

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commitNowAllowingStateLoss()

            return@setOnNavigationItemSelectedListener true
        }

        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.navigation_explore
        } else {
            val helperState = savedInstanceState.getBundle(STATE_HELPER_KEY)!!
            stateHelper.restoreHelperState(helperState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveCurrentState()
        outState.putBundle(STATE_HELPER_KEY, stateHelper.saveHelperState())
    }

    private fun getFragmentById(itemId: Int): Fragment = when (itemId) {
        R.id.navigation_favorites -> {
            FavoritesFragment()
        }
        R.id.navigation_history -> {
            HistoryFragment()
        }
        else -> {
            ExploreFragment()
        }
    }

    private fun saveCurrentState() {
        fragments[navigation.selectedItemId]?.let { oldFragment->
            stateHelper.saveState(oldFragment, navigation.selectedItemId)
        }
    }
}
