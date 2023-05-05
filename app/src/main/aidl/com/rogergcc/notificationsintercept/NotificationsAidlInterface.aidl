// NotificationsAidlInterface.aidl
package com.rogergcc.notificationsintercept;

import com.rogergcc.notificationsintercept.NotificationInfo;


interface NotificationsAidlInterface {

    List<NotificationInfo> getNotificationInfoList();

    void addInfo(inout NotificationInfo notificationInfo);
}