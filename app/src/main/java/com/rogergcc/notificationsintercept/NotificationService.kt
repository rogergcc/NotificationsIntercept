package com.rogergcc.notificationsintercept

import android.app.Notification
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.NOTIFICATIONCODE
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.OWN_PACKAGE
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.TELEGRAM_PACK_NAME
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.YAPE_PACK_NAME
import com.rogergcc.notificationsintercept.ui.utils.TimberAppLogger
import kotlinx.coroutines.*

class NotificationService() : NotificationListenerService() {
    private var notificationsAidInterface: NotificationsAidlInterface? = null
    private var bound = false
    private var notificationInfoList: List<NotificationInfo> = listOf()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        TimberAppLogger.e("onbind called - ON CREATE")
        return super.onBind(intent)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            notificationsAidInterface = NotificationsAidlInterface.Stub.asInterface(service)
            bound = true

            if (notificationsAidInterface != null) {
                try {
                    notificationInfoList =
                        notificationsAidInterface?.notificationInfoList ?: listOf()
                } catch (exception: RemoteException) {
                    exception.printStackTrace()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }


    override fun onDestroy() {
        TimberAppLogger.e("onDestroy called - BYE 🥱")
        super.onDestroy()
        //job.cancel()
    }

    override fun onListenerConnected() {
        TimberAppLogger.e("onListenerConnected called - INICIADO")
        super.onListenerConnected()

    }

    override fun onListenerDisconnected() {
        TimberAppLogger.e("onListenerDisconnected called - BYE 🥱")

        super.onListenerDisconnected()
    }

//    override fun onNotificationRemoved(sbn: StatusBarNotification) {
//        Log.e(TAG, "onNotificationRemoved called")
//        val activeNotification = this.activeNotifications
//
//        for (i in activeNotification) {
//            val packageName = i.packageName.toString()
//            if ((packageName == TELEGRAM_PACK_NAME) or (packageName == YAPE_PACK_NAME)) {
//                val extras = sbn.notification.extras
//                val text: String = extras.getCharSequence("android.text").toString()
//                val intent = Intent(OWN_PACKAGE).apply {
//                    putExtra(NOTIFICATIONCODE, text)
//                    sendBroadcast(this)
//                }
//                break
//            }
//        }
////        super.onNotificationRemoved(sbn)
//    }


    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        super.onNotificationPosted(sbn)

        sbn?.let {
            displayOnLogNotificationInfo(it)

            val packageName = it.packageName.toString()
            try {
                if ((packageName == TELEGRAM_PACK_NAME) or (packageName == YAPE_PACK_NAME)) {
                    val title = it.notification.extras.getString(Notification.EXTRA_TITLE)
                    val text = it.notification.extras.getString(Notification.EXTRA_TEXT)
                    val notificationData = NotificationData(title = title, text = text)
                    //showCustomNotification(title + , "from Posted" +text)

//                // Enviar datos al servidor
                    //sendDatoToServer(notificationData)

                    val intent = Intent(OWN_PACKAGE)
                    intent.putExtra(NOTIFICATIONCODE, "Posted=> title=> $title text=> $text")
                    sendBroadcast(intent)
                } else {
                    TimberAppLogger.i("other Package $packageName")

                }
            } catch (ex: Exception) {
                printExceptionInfo(ex)
            }
        }


//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    }

    data class NotificationData(
        val title: String?,
        val text: String?,
    )

    private fun printExceptionInfo(ex: Exception) {
        TimberAppLogger.e(TAG, "Exception: $ex")
        TimberAppLogger.e(TAG, "Exception: ${ex.localizedMessage}")
    }

    private fun displayOnLogNotificationInfo(sbn: StatusBarNotification) {
        TimberAppLogger.e(TAG, "onNotificationPosted1 called - RECIBIDOS 😀")
        TimberAppLogger.i(TAG, "--------------------------------------------------")
        TimberAppLogger.i(TAG, "ID : ${sbn.id}")
        TimberAppLogger.i(TAG, "Package name : ${sbn.packageName}")
        TimberAppLogger.i(TAG, "Post time : ${sbn.postTime}")
        TimberAppLogger.i(TAG, "Tag : ${sbn.tag}")
        val notification = sbn.notification
        TimberAppLogger.i(
            TAG, "Title : ${
                notification.extras
                    .getCharSequence("android.title")
            }"
        )
        TimberAppLogger.i(
            TAG, "Title : ${
                notification.extras
                    .getCharSequence("android.text")
            }"
        )
        TimberAppLogger.i(TAG, "--------------------------------------------------")

    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        GlobalScope.launch {
            val errorModelMapper = exception.message.orEmpty()
        }
    }

    private fun addNotification(title: String, message: String) {
        val info = NotificationInfo(title, message)
        Log.e(TAG, "addNotification: ")
        Log.e(TAG, "addNotification: info $info")
        try {
            notificationsAidInterface?.addInfo(info)
        } catch (e: RemoteException) {
            Log.e(TAG, "addNotification: ${e.message}")
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        TimberAppLogger.e(TAG, "onNotificationPosted2 called - RECIBIDOS 😀")

        super.onNotificationPosted(sbn, rankingMap)

    }

    companion object {
        private const val TAG = "NotificationService"
    }
}

