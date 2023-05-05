package com.rogergcc.notificationsintercept.data.remote

import retrofit2.Response


interface NotificationsCloudDataSource {
    suspend fun sendNotification(data: String): Response<ApiScriptResponse>
}