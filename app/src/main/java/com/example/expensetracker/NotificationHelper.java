package com.example.expensetracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "expense_channel";
    private static final String CHANNEL_NAME = "消费记录通知";
    private static final int NOTIFICATION_ID = 1001;

    public static void showExpenseAddedNotification(Context context, String title, String amount) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 创建通知渠道（Android 8.0+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("消费记录添加通知");
            notificationManager.createNotificationChannel(channel);
        }

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_add)
                .setContentTitle("消费记录已添加")
                .setContentText(title + " " + amount)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // 显示通知
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
