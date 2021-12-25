package com.readrops.api.services.fever

import com.readrops.api.services.fever.adapters.FeverAPIAdapter
import com.readrops.api.utils.ApiUtils
import com.readrops.db.entities.Feed
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody

class FeverDataSource(private val service: FeverService) {

    suspend fun login(login: String, password: String) {
        val credentials = ApiUtils.md5hash("$login:$password")

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("api_key", credentials)
            .build()

        val response = service.login(requestBody)

        val adapter = Moshi.Builder()
            .add(Boolean::class.java, FeverAPIAdapter())
            .build()
            .adapter(Boolean::class.java)

        adapter.fromJson(response.source())!!
    }

    suspend fun getFeeds(): List<Feed> = service.getFeeds()
}