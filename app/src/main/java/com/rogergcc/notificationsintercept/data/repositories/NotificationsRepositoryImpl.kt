package com.rogergcc.notificationsintercept.data.repositories

import com.rogergcc.notificationsintercept.data.remote.ApiScriptResponse
import com.rogergcc.notificationsintercept.data.remote.NotificationsCloudDataSource
import com.rogergcc.notificationsintercept.domain.repositories.NotificationRepository
import retrofit2.Response

class NotificationsRepositoryImpl(
    private val notificationsCloudDataSource: NotificationsCloudDataSource,
) : NotificationRepository {

    override suspend fun sendNotificationR(data: String): Response<ApiScriptResponse> {

        return notificationsCloudDataSource.sendNotification(data)

    }
}
