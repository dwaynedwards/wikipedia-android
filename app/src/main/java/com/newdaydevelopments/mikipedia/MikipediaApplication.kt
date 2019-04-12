package com.newdaydevelopments.mikipedia

import android.app.Application
import com.newdaydevelopments.mikipedia.managers.MikipediaManager
import com.newdaydevelopments.mikipedia.providers.PageDataProvider
import com.newdaydevelopments.mikipedia.repositories.PageDatabaseOpenHelper
import com.newdaydevelopments.mikipedia.repositories.FavoritesRepository
import com.newdaydevelopments.mikipedia.repositories.HistoryRepository

class MikipediaApplication: Application() {

    lateinit var mikipediaManager: MikipediaManager
        private set

    override fun onCreate() {
        super.onCreate()

        val provider = PageDataProvider()
        val dbHelper = PageDatabaseOpenHelper(applicationContext)
        val favoritesRepository = FavoritesRepository(dbHelper)
        val historyRepository = HistoryRepository(dbHelper)
        mikipediaManager = MikipediaManager(provider, favoritesRepository, historyRepository)
    }
}