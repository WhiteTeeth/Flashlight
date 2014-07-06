package baiya.flashlight.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import baiya.flashlight.R;
import baiya.flashlight.ScreenLightActivity;


/**
 * Implementation of App Widget functionality.
 */
public class ScreenLightWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.screen_light_widget);
        PendingIntent pendingIntent = ScreenLightActivity.getPendingIntent(context, appWidgetId);
        views.setOnClickPendingIntent(R.id.widget_screen, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


