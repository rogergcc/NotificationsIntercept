package com.rogergcc.notificationsintercept

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NotificationInfo(var title: String, var message: String) : Parcelable {

    fun readFromParcel(parcel: Parcel) {
        title = parcel.readString() ?: ""
        message = parcel.readString() ?: ""
    }

}