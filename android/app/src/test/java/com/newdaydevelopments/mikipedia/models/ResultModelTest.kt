package com.newdaydevelopments.mikipedia.models

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Test

import kotlin.random.Random
import kotlin.random.nextInt

class ResultModelTest {

    @Test
    fun setAndGetQuery() {
        val pageId = Random.nextInt(1..100)
        val title = Random.nextInt(1..100).toString()
        val fullUrl = Random.nextInt(1..100).toString()
        val source = Random.nextInt(1..100).toString()
        val width = Random.nextInt(1..100)
        val height = Random.nextInt(1..100)
        val resultJson = buildResultJson(pageId, title, fullUrl, source, width, height)
        val result =
            ResultModel(
                QueryModel(arrayListOf(PageModel(pageId, title, fullUrl, ThumbnailModel(source, width, height))))
            )
        val resultTest = Gson().fromJson<ResultModel>(resultJson, ResultModel::class.java)
        assertEquals(result, resultTest)
        val resultJsonTest = Gson().toJson(resultTest)
        assertEquals(resultJson, resultJsonTest)
    }

    private fun buildResultJson(
        pageId: Int, title: String, fullUrl: String,
        source: String, width: Int, height: Int
    ): String {
        return "{\"query\":{\"pages\":[{\"pageid\":$pageId,\"title\":\"$title\",\"fullurl\":\"$fullUrl\"," +
                "\"thumbnail\":{\"source\":\"$source\",\"width\":$width,\"height\":$height}}]}}"
    }
}