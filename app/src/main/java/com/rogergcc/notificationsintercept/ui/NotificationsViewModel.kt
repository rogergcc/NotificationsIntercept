package com.rogergcc.notificationsintercept.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogergcc.notificationsintercept.domain.GetNotificationsUseCase
import com.rogergcc.notificationsintercept.ui.utils.TimberAppLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationUseCase: GetNotificationsUseCase,

    ) : ViewModel() {


    fun getNotifications(data: String) = viewModelScope.launch(handler) {
        launch(Dispatchers.IO) {

            val result = getNotificationUseCase.sendNotificationUC(data)
            TimberAppLogger.i(TAG, result)
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            val errorModelMapper = exception.message.orEmpty()
            TimberAppLogger.e(TAG, "errorModelMapper:  $errorModelMapper")
        }
    }

    companion object {
        private const val TAG = "NotificationsViewModel"
    }
}