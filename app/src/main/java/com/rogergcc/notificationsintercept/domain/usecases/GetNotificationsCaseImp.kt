package com.rogergcc.notificationsintercept.domain.usecases

import com.rogergcc.notificationsintercept.data.remote.ApiScriptResponse
import com.rogergcc.notificationsintercept.domain.GetNotificationsUseCase
import com.rogergcc.notificationsintercept.domain.repositories.NotificationRepository
import retrofit2.Response

class GetNotificationsCaseImp(
    private val notificationsRepository: NotificationRepository,
) : GetNotificationsUseCase {

    override suspend fun sendNotificationUC(data: String): Response<ApiScriptResponse> {
        val notification = notificationsRepository.sendNotificationR(data)
        return notification
    }

    companion object {
        private const val TAG = "GetNotificationsCaseImp"
    }
}