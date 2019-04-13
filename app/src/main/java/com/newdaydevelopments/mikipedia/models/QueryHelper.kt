package com.newdaydevelopments.mikipedia.models

object QueryHelper {
    private const val BASE_URL = "https://en.wikipedia.org/w/api.php"

    fun getSearchUrl(term: String, take: Int, skip: Int): String {
        return BASE_URL +
                "?format=json&action=query&formatversion=2&generator=prefixsearch" +
                "&gpssearch=$term&gpslimit=$take&gpsoffset=$skip&prop=pageimages|info" +
                "&piprop=thumbnail|url&pithumbsize=200&pilimit=$take&wbptterms=description" +
                "&inprop=url"
    }

    fun getRandomUrl(take: Int): String {
        return BASE_URL +
                "?format=json&action=query&formatversion=2&generator=random&grnnamespace=0" +
                "&prop=pageimages|info&pithumbsize=200&grnlimit=$take&inprop=url"
    }
}