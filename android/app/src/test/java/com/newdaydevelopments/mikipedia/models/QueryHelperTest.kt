package com.newdaydevelopments.mikipedia.models

import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random
import kotlin.random.nextInt

class QueryHelperTest {

    @Test
    fun getSearchUrl() {
        val randomTerm = Random.nextInt(1..100).toString()
        val randomTake = Random.nextInt(1..100)
        val randomSkip = Random.nextInt(1..100)
        val url = "https://en.wikipedia.org/w/api.php?format=json&action=query" +
                            "&formatversion=2&generator=prefixsearch&gpssearch=$randomTerm" +
                            "&gpslimit=$randomTake&gpsoffset=$randomSkip&prop=pageimages|info" +
                            "&piprop=thumbnail|url&pithumbsize=200&pilimit=$randomTake" +
                            "&wbptterms=description&inprop=url"
        val testUrl = QueryHelper.getSearchUrl(randomTerm,randomTake, randomSkip)
        assertEquals(url, testUrl)
    }

    @Test
    fun getRandomUrl() {
        val randomTake = Random.nextInt(1..100)
        val url = "https://en.wikipedia.org/w/api.php?format=json" +
                            "&action=query&formatversion=2&generator=random" +
                            "&grnnamespace=0&prop=pageimages|info&pithumbsize=200" +
                            "&grnlimit=$randomTake&inprop=url"
        val testUrl = QueryHelper.getRandomUrl(randomTake)
        assertEquals(url, testUrl)
    }
}