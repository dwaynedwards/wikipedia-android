package com.newdaydevelopments.wikipedia.repositories

import com.google.gson.Gson
import com.newdaydevelopments.wikipedia.models.WikiPageModel
import com.newdaydevelopments.wikipedia.models.WikiThumbnailModel
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

abstract class DatabaseRepository(private val db: PageDatabaseOpenHelper, private val tableName: String) {

    fun addPage(page: WikiPageModel) {
        db.use {
            insert(
                tableName,
                "id" to page.pageid,
                "title" to page.title,
                "url" to page.fullurl,
                "thumbnailJson" to Gson().toJson(page.thumbnail)
            )
        }
    }

    fun removePageById(pageId: Int) {
        db.use {
            delete(
                tableName,
                "id = {pageId}",
                "pageId" to pageId
            )
        }
    }

    fun removeAll() {
        db.use {
            delete(tableName)
        }
    }

    fun hasPage(pageId: Int): Boolean {
        val pages = getAllPages()
        return pages.any { page ->
            page.pageid == pageId
        }
    }

    fun getAllPages(): ArrayList<WikiPageModel> {
        val pages = ArrayList<WikiPageModel>()

        db.use {
            select(tableName).parseList(
                rowParser { id: Int, title: String, url: String, thumbnailJson: String ->
                    val thumbnail = Gson().fromJson(thumbnailJson, WikiThumbnailModel::class.java)
                    pages.add(WikiPageModel(id, title, url, thumbnail))
                }
            )
        }
        return pages
    }
}