package com.newdaydevelopments.wikipedia.providers

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.newdaydevelopments.wikipedia.models.QueryUtils
import com.newdaydevelopments.wikipedia.models.WikiResultModel
import java.io.Reader

class PageDataProvider {

    init {
        FuelManager.instance.baseHeaders = mapOf("User-Agent" to "NDD Wikipedia")
    }

    fun search(term: String, take: Int, skip: Int, handler: (result: WikiResultModel) -> Unit) {
        QueryUtils.getSearchUrl(term, take, skip)
            .httpGet().responseObject(WikiDataDeserializer()) { _, response, result ->
                if (response.statusCode != 200) {
                    throw Exception("Unable to get articles")
                }
                val (data, _) = result
                handler.invoke(data as WikiResultModel)
        }
    }

    fun getRandom(take: Int, handler: (result: WikiResultModel) -> Unit) {
        QueryUtils.getRandomUrl(take)
            .httpGet().responseObject(WikiDataDeserializer()) { _, response, result ->
                if (response.statusCode != 200) {
                    throw Exception("Unable to get articles")
                }
                val (data, _) = result
                handler.invoke(data as WikiResultModel)
        }
    }

    class WikiDataDeserializer: ResponseDeserializable<WikiResultModel> {
        override fun deserialize(reader: Reader): WikiResultModel?
                = Gson().fromJson(reader, WikiResultModel::class.java)
    }
}