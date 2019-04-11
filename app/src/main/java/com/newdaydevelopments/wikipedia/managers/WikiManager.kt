package com.newdaydevelopments.wikipedia.managers

import com.newdaydevelopments.wikipedia.models.WikiPageModel
import com.newdaydevelopments.wikipedia.models.WikiResultModel
import com.newdaydevelopments.wikipedia.providers.PageDataProvider
import com.newdaydevelopments.wikipedia.repositories.FavoritesRepository
import com.newdaydevelopments.wikipedia.repositories.HistoryRepository

class WikiManager(private val provider: PageDataProvider,
                  private val favoritesRepository: FavoritesRepository,
                  private val historyRepository: HistoryRepository) {

    private var favoritesCache: ArrayList<WikiPageModel>? = null
    private var historyCache: ArrayList<WikiPageModel>? = null

    fun search(term: String, take: Int, skip: Int, handler: (result: WikiResultModel) -> Unit) {
        provider.search(term, take, skip, handler)
    }

    fun getRandom(take: Int, handler: (result: WikiResultModel) -> Unit) {
        provider.getRandom(take, handler)
    }

    fun getFavorites(): ArrayList<WikiPageModel> {
        if (favoritesCache == null) {
            favoritesCache = favoritesRepository.getAllPages()
        }
        return favoritesCache!!
    }

    fun getHistory(): ArrayList<WikiPageModel> {
        if (historyCache == null) {
            historyCache = historyRepository.getAllPages()
        }
        return historyCache!!
    }

    fun addFavorite(page: WikiPageModel) {
        favoritesCache?.add(page)
        favoritesRepository.addPage(page)
    }

    fun removeFavorite(pageId: Int) {
        favoritesRepository.removePageById(pageId)
        favoritesCache = favoritesCache?.filter { it.pageid != pageId } as ArrayList<WikiPageModel>
    }

    fun clearFavorites() {
        favoritesCache?.clear()
        favoritesRepository.removeAll()
    }

    fun isFavorite(pageId: Int): Boolean {
        return favoritesRepository.hasPage(pageId)
    }

    fun addHistory(page: WikiPageModel) {
        historyCache?.add(page)
        historyRepository.addPage(page)
    }

    fun removeHistory(pageId: Int) {
        historyRepository.removePageById(pageId)
        historyCache = historyCache?.filter { it.pageid != pageId } as ArrayList<WikiPageModel>
    }

    fun clearHistory() {
        historyCache?.clear()
        historyRepository.removeAll()
    }

    fun hasHistory(pageId: Int): Boolean {
        return historyRepository.hasPage(pageId)
    }
}
