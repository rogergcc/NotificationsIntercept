package com.rogergcc.notificationsintercept

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.rogergcc.notificationsintercept.StringUtils.NOTIFICATIONCODE
import com.rogergcc.notificationsintercept.StringUtils.OWN_PACKAGE
import com.rogergcc.notificationsintercept.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = checkNotNull(_binding)
    //
    var progressDialog = BaseProgressDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)


//        registerReceiver(mHandler, IntentFilter("net.kwmt27.notificationlistenersample"))

        //createNotificationChannel()

//        binding.notify.setOnClickListener {
//            val builder = NotificationCompt.aBuilder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("TitleContent")
//                .setContentText("ContenText")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//            with(NotificationManagerCompat.from(this)) {
//                // notificationId is a unique int for each notification that you must define
//                notify(notificationId++, builder.build())
//            }
//        }
//
        if (!isNotificationServiceEnabled()) {

//            enableNotificationListenerAlertDialog.show()
            binding.notify.setVisibility(View.VISIBLE)

        } else {
            binding.notify.setVisibility(View.GONE)
        }

        binding.notify.setOnClickListener {
            requestPushPermission()
        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
//
//            addContact.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    pickContactIntent();
//                }
//            });
//        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received


//        startService(Intent(this, NotificationService::class.java))
        registerPushListener()
    }

    private fun registerPushListener() {
        val intentFilter = IntentFilter().apply {
            addAction(OWN_PACKAGE)
        }
        Log.e(TAG,"IS ORDER?: "+mHandler.isOrderedBroadcast)
        Log.e(TAG,"IS ORDER?: "+mHandler.isInitialStickyBroadcast)
        registerReceiver(mHandler, intentFilter)

    }

    private fun requestPushPermission() {
//        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver,
            MainActivity.ENABLED_NOTIFICATION_LISTENERS)
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
                Log.e(TAG, receivedNotificationCode)
//                val codigoUsuario = intent.getStringExtra("codigo")
//                val nombreUsuario = intent.getStringExtra("nombreUsuario")
                //addItemToApiRestService("$OWN_PACKAGE - $receivedNotificationCode")
            }catch (ex :Exception){
                Log.e(TAG, "onReceive: ${ex.message}" )
            }

        }
    }

    companion object {


        private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private const val TAG = "MainActivity"
        private const val CHANNEL_ID = "channel_id"
        var notificationId = 0
    }
}
