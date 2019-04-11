package com.newdaydevelopments.wikipedia

import android.app.Application
import com.newdaydevelopments.wikipedia.managers.WikiManager
import com.newdaydevelopments.wikipedia.providers.PageDataProvider
import com.newdaydevelopments.wikipedia.repositories.PageDatabaseOpenHelper
import com.newdaydevelopments.wikipedia.repositories.FavoritesRepository
import com.newdaydevelopments.wikipedia.repositories.HistoryRepository

class WikiApplication: Application() {

    lateinit var wikiManager: WikiManager
        private set

    override fun onCreate() {
        super.onCreate()

        val wikiProvider = PageDataProvider()
        val dbHelper = PageDatabaseOpenHelper(applicationContext)
        val favoritesRepository = FavoritesRepository(dbHelper)
        val historyRepository = HistoryRepository(dbHelper)
        wikiManager = WikiManager(wikiProvider, favoritesRepository, historyRepository)
    }
}