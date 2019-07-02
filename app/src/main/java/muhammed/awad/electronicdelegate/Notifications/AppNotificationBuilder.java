package muhammed.awad.electronicdelegate.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import muhammed.awad.electronicdelegate.R;

public class AppNotificationBuilder {

    private static final String TAG = AppNotificationBuilder.class.getName();
    private static final int NOTIFICATION_ID = 0;

    public static void pushNotification(String title, String body, Context context) {
        Log.v(TAG, "pushNotification");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id));

        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pushRegisterChannelNotification(context);
        } else {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    private static void pushRegisterChannelNotification(Context context) {
        Log.v(TAG, "pushChannelNotification");

        final String channel_id = context.getString(R.string.default_notification_channel_id);
        final String description = context.getString(R.string.default_notification_channel_des);
        final int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notificationChannel = new NotificationChannel(channel_id, description, importance);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
