package com.newdaydevelopments.mikipedia.models

data class ResultModel(var query: QueryModel? = null)

data class QueryModel(val pages: ArrayList<PageModel> = ArrayList())

data class PageModel(
    var pageid: Int,
    var title: String,
    var fullurl: String,
    var thumbnail: ThumbnailModel?
)

data class ThumbnailModel(var source: String?, var width: Int = 0, var height: Int = 0)