package muhammed.awad.electronicdelegate.Notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;

import muhammed.awad.electronicdelegate.R;

public class AppMessagingService extends FirebaseMessagingService {

    private static final String TAG = AppMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v(TAG, "MessageReceived");
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String notificationTitle = remoteMessage.getData().get("title");
        String notificationBody = remoteMessage.getData().get("message");

        try {
            boolean isAppRunning = new RunningTaskManager().execute(this).get();
            if (isAppRunning) {
                Log.v(TAG, "Foreground");
            } else {
                Log.v(TAG, "Background");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

//        if (notification != null) {
//            try {
//                if (notification.getTitle() != null) {
//                    notificationTitle = notification.getTitle();
//                } else {
//                    notificationTitle = getString(R.string.app_name);
//                }
//                notificationBody = notification.getBody();

        AppNotificationBuilder.pushNotification(
                notificationTitle,
                notificationBody,
                getApplicationContext());

//            catch (NullPointerException exception) {
//                Log.e(TAG, exception.getMessage());
//            }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "NEW_TOKEN " + s);
        String token = FirebaseInstanceId.getInstance().getToken();
    }
}
