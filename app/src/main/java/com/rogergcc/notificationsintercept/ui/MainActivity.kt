package com.rogergcc.notificationsintercept.ui

import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.rogergcc.notificationsintercept.databinding.ActivityMainBinding
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.NOTIFICATIONCODE
import com.rogergcc.notificationsintercept.ui.utils.StringUtils.OWN_PACKAGE
import com.rogergcc.notificationsintercept.ui.utils.TimberAppLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding)

    //
    private val notificationsViewModel: NotificationsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRequestAccess.setOnClickListener {
            requestPushPermission()


        }

        askForPermissionIfNecessary()

        // Finally we register a receiver to tell the MainActivity when a notification has been received

//        startService(Intent(this, NotificationService::class.java))

        registerPushListener()
    }

    private fun registerPushListener() {
        val intentFilter = IntentFilter().apply {
            addAction(OWN_PACKAGE)
        }
        printDetails(mHandler)
        registerReceiver(mHandler, intentFilter)

    }

    private fun printDetails(mHandler: BroadcastReceiver) {
        TimberAppLogger.e(TAG, "isInitialStickyBroadcast: ${mHandler.isInitialStickyBroadcast}")
        TimberAppLogger.i(TAG, "isOrderedBroadcast: ${mHandler.isOrderedBroadcast}")
    }

    private fun askForPermissionIfNecessary() {
        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            val intent = Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    private fun requestPushPermission() {
//        val intent = Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }


    private val mHandler: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                //            if(getIntent().getExtras() != null) {
                val receivedNotificationCode =
                    intent.getStringExtra(NOTIFICATIONCODE).toString().trim()
//                val codigoUsuario = intent.getStringExtra("codigo")
//                val nombreUsuario = intent.getStringExtra("nombreUsuario")
                //addItemToApiRestService("$OWN_PACKAGE - $receivedNotificationCode")
                notificationsViewModel.getNotifications(receivedNotificationCode)
            }catch (ex :Exception){
                TimberAppLogger.e(TAG, "erro ${ex.message}")
            }

        }
    }

    companion object {

        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private const val TAG = "MainActivity"
        private const val CHANNEL_ID = "channel_id"
        var notificationId = 0
    }
}
