package com.rogergcc.notificationsintercept.data.remote

import com.rogergcc.notificationsintercept.data.remote.api.ApiService
import retrofit2.Response

class CloudDataSourceImp(
    private val notificationService: ApiService,
) : NotificationsCloudDataSource {

    override suspend fun sendNotification(data: String): Response<ApiScriptResponse> {
        return notificationService.postNotificationData(data)
    }

    companion object {

        private const val TAG = "CloudDataSourceImp"
    }
}
