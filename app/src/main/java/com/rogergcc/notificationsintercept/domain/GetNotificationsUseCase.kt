package com.rogergcc.notificationsintercept.domain

import com.rogergcc.notificationsintercept.data.remote.ApiScriptResponse
import retrofit2.Response

interface GetNotificationsUseCase {
    suspend fun sendNotificationUC(data: String): Response<ApiScriptResponse>
}