package com.rogergcc.notificationsintercept

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.rogergcc.notificationsintercept.StringUtils.NOTIFICATIONCODE
import com.rogergcc.notificationsintercept.StringUtils.OWN_PACKAGE
import com.rogergcc.notificationsintercept.StringUtils.TELEGRAM_PACK_NAME
import com.rogergcc.notificationsintercept.StringUtils.YAPE_PACK_NAME

class NotificationService : NotificationListenerService() {
    val TAG = this::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onbind called - ON CREATE")
        return super.onBind(intent)
    }
    override fun onListenerConnected() {
        Log.e(TAG, "onListenerConnected called - INICIADO")
        super.onListenerConnected()

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.e(TAG, "onNotificationRemoved called")
        val activeNotification = this.activeNotifications

        for (i in activeNotification) {
            val packageName = i.packageName.toString()
            if ((packageName == TELEGRAM_PACK_NAME) or (packageName == YAPE_PACK_NAME)) {
                val extras = sbn.notification.extras
                val text: String = extras.getCharSequence("android.text").toString()
                val intent = Intent(OWN_PACKAGE).apply {
                    putExtra(NOTIFICATIONCODE, text)
                    sendBroadcast(this)
                }
                break
            }
        }
//        super.onNotificationRemoved(sbn)
    }

    override fun onListenerDisconnected() {
        Log.e(TAG, "onListenerDisconnected called - BYE 🥱")
        super.onListenerDisconnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        super.onNotificationPosted(sbn)
        Log.e(TAG, "onNotificationPosted1 called - RECIBIDOS 😀")
        val packageName = sbn.packageName.toString()
        Log.e(TAG, "paquete $packageName")
//        if(!paquete.equals("com.bcp.innovacxion.yapeapp")) return
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            if ((packageName == TELEGRAM_PACK_NAME) or (packageName == YAPE_PACK_NAME)) {
//            Log.e(TAG, "paquete $paquete")
                val extras = sbn.notification.extras
                val text: String = extras.getCharSequence("android.text").toString()
                Log.e(TAG, "notificacion Data $text")

                val intent = Intent("net.kwmt27.notificationlistenersample")
                intent.putExtra(NOTIFICATIONCODE, text)
                sendBroadcast(intent)
            }else{
                Log.e(TAG, "OTHER PACKAGES")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Exception: $ex")
            Log.e(TAG, "Exception: ${ex.localizedMessage}")
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        Log.e(TAG, "onNotificationPosted2 called")
        super.onNotificationPosted(sbn, rankingMap)

    }
}

