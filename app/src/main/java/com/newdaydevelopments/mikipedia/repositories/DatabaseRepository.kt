package com.newdaydevelopments.mikipedia.repositories

import com.google.gson.Gson
import com.newdaydevelopments.mikipedia.models.PageModel
import com.newdaydevelopments.mikipedia.models.ThumbnailModel
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

abstract class DatabaseRepository(private val db: PageDatabaseOpenHelper, private val tableName: String) {

    fun addPage(page: PageModel) {
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

    fun getAllPages(): ArrayList<PageModel> {
        val pages = ArrayList<PageModel>()

        db.use {
            select(tableName).parseList(
                rowParser { id: Int, title: String, url: String, thumbnailJson: String ->
                    val thumbnail = Gson().fromJson(thumbnailJson, ThumbnailModel::class.java)
                    pages.add(PageModel(id, title, url, thumbnail))
                }
            )
        }
        return pages
    }
}