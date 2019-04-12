package com.newdaydevelopments.mikipedia.managers

import com.newdaydevelopments.mikipedia.models.PageModel
import com.newdaydevelopments.mikipedia.models.ResultModel
import com.newdaydevelopments.mikipedia.providers.PageDataProvider
import com.newdaydevelopments.mikipedia.repositories.FavoritesRepository
import com.newdaydevelopments.mikipedia.repositories.HistoryRepository

class MikipediaManager(private val provider: PageDataProvider,
                       private val favoritesRepository: FavoritesRepository,
                       private val historyRepository: HistoryRepository
) {

    private var favoritesCache: ArrayList<PageModel>? = null
    private var historyCache: ArrayList<PageModel>? = null

    fun search(term: String, take: Int, skip: Int, handler: (result: ResultModel) -> Unit) {
        provider.search(term, take, skip, handler)
    }

    fun getRandom(take: Int, handler: (result: ResultModel) -> Unit) {
        provider.getRandom(take, handler)
    }

    fun getFavorites(): ArrayList<PageModel> {
        if (favoritesCache == null) {
            favoritesCache = favoritesRepository.getAllPages()
        }
        return favoritesCache!!
    }

    fun getHistory(): ArrayList<PageModel> {
        if (historyCache == null) {
            historyCache = historyRepository.getAllPages()
        }
        return historyCache!!
    }

    fun addFavorite(page: PageModel) {
        favoritesCache?.add(page)
        favoritesRepository.addPage(page)
    }

    fun removeFavorite(pageId: Int) {
        favoritesRepository.removePageById(pageId)
        favoritesCache = favoritesCache?.filter { it.pageid != pageId } as ArrayList<PageModel>
    }

    fun clearFavorites() {
        favoritesCache?.clear()
        favoritesRepository.removeAll()
    }

    fun isFavorite(pageId: Int): Boolean {
        return favoritesRepository.hasPage(pageId)
    }

    fun addHistory(page: PageModel) {
        historyCache?.add(page)
        historyRepository.addPage(page)
    }

    fun removeHistory(pageId: Int) {
        historyRepository.removePageById(pageId)
        historyCache = historyCache?.filter { it.pageid != pageId } as ArrayList<PageModel>
    }

    fun clearHistory() {
        historyCache?.clear()
        historyRepository.removeAll()
    }

    fun hasHistory(pageId: Int): Boolean {
        return historyRepository.hasPage(pageId)
    }
}
