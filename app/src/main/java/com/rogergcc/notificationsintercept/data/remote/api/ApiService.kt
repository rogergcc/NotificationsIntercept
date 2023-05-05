package com.rogergcc.notificationsintercept.data.remote.api

import com.rogergcc.notificationsintercept.data.remote.ApiScriptResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("exec")
    suspend fun sendNotificationData(@Query("sdata") sdata: String): ResponseBody

    @POST("exec")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun postNotificationData(@Query("sdata") dataQR: String): Response<ApiScriptResponse>
}

