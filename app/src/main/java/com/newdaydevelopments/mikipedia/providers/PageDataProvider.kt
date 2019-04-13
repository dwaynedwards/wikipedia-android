package com.newdaydevelopments.mikipedia.providers

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.newdaydevelopments.mikipedia.models.QueryHelper
import com.newdaydevelopments.mikipedia.models.ResultModel
import java.io.Reader

class PageDataProvider {

    init {
        FuelManager.instance.baseHeaders = mapOf("User-Agent" to "NDD Mikipedia")
    }

    fun search(term: String, take: Int, skip: Int, handler: (result: ResultModel) -> Unit) {
        QueryHelper.getSearchUrl(term, take, skip)
            .httpGet().responseObject(MikipediaDataDeserializer()) { _, response, result ->
                val (data, error) = result
                if (error != null) {
                    throw Exception("Error getting articles: ${error.message}")
                }
                if (response.statusCode != 200 || data == null) {
                    throw Exception("Unable to get articles")
                }
                handler.invoke(data)
        }
    }

    fun getRandom(take: Int, handler: (result: ResultModel) -> Unit) {
        QueryHelper.getRandomUrl(take)
            .httpGet().responseObject(MikipediaDataDeserializer()) { _, response, result ->
                val (data, error) = result
                if (error != null) {
                    throw Exception("Error getting articles: ${error.message}")
                }
                if (response.statusCode != 200 || data == null) {
                    throw Exception("Unable to get articles")
                }
                handler.invoke(data)
        }
    }

    class MikipediaDataDeserializer: ResponseDeserializable<ResultModel> {
        override fun deserialize(reader: Reader): ResultModel?
                = Gson().fromJson(reader, ResultModel::class.java)
    }
}