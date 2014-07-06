package baiya.flashlight.controler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import baiya.flashlight.R;
import baiya.flashlight.widget.FlashLightWidget;

public class NotificationControl {

    public static final int sNotificationId = 318;

    public static void showNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_text));

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, new Intent(FlashLightWidget.ACTION_LED_OFF), 0);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(sNotificationId, builder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(sNotificationId);
    }
}
