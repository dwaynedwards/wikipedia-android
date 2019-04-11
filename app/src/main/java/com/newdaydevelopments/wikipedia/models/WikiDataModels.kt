package com.newdaydevelopments.wikipedia.models

data class WikiResultModel(var query: WikiQueryModel? = null)

data class WikiQueryModel(val pages: ArrayList<WikiPageModel> = ArrayList())

data class WikiPageModel(
    var pageid: Int,
    var title: String,
    var fullurl: String,
    var thumbnail: WikiThumbnailModel?
)

data class WikiThumbnailModel(var source: String?, var width: Int = 0, var height: Int = 0)