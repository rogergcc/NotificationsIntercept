package com.rogergcc.notificationsintercept.domain.repositories

import com.rogergcc.notificationsintercept.data.remote.ApiScriptResponse
import retrofit2.Response


interface NotificationRepository {
    suspend fun sendNotificationR(data: String): Response<ApiScriptResponse>
}