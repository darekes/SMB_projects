package pl.dsamsel.mp5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import static pl.dsamsel.mp5.WidgetActionConstants.GO_TO_NEXT_IMAGE;
import static pl.dsamsel.mp5.WidgetActionConstants.GO_TO_PREVIEW_IMAGE;
import static pl.dsamsel.mp5.WidgetActionConstants.OPEN_BROWSER;

public class MultimediaAppWidget extends AppWidgetProvider {

    private static final String URL = "https://warszawskisalonjachtowy.pl/en/";
    private static int imageCounter;
    private static final int[] IMAGES_ARRAY = new int[]{R.drawable.sail_1, R.drawable.sail_2};

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_app_widget);

        views.setOnClickPendingIntent(R.id.button_open_browser, getPendingSelfIntent(context, OPEN_BROWSER));
        views.setOnClickPendingIntent(R.id.button_image_previous, getPendingSelfIntent(context, GO_TO_PREVIEW_IMAGE));
        views.setOnClickPendingIntent(R.id.button_image_next, getPendingSelfIntent(context, GO_TO_NEXT_IMAGE));

        imageCounter = 0;
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, MultimediaAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        switch (action) {
            case OPEN_BROWSER:
                handleOpenBrowserPressed(context);
                break;
            case GO_TO_PREVIEW_IMAGE:
                handlePreviousImagePressed(context);
                break;
            case GO_TO_NEXT_IMAGE:
                handleNextImagePressed(context);
                break;

            default:
                Log.e(this.getClass().getSimpleName(), "Invalid action name");
        }
    }

    private void handleOpenBrowserPressed(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }

    private void handlePreviousImagePressed(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_app_widget);
        if (imageCounter == 0) {
            imageCounter = IMAGES_ARRAY.length - 1;
            views.setImageViewResource(R.id.image_sail_1, IMAGES_ARRAY[imageCounter]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MultimediaAppWidget.class), views);
        } else {
            imageCounter = imageCounter - 1;
            views.setImageViewResource(R.id.image_sail_1, IMAGES_ARRAY[imageCounter]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MultimediaAppWidget.class), views);
        }
    }

    private void handleNextImagePressed(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_app_widget);
        if (imageCounter == IMAGES_ARRAY.length - 1) {
            imageCounter = 0;
            views.setImageViewResource(R.id.image_sail_1, IMAGES_ARRAY[imageCounter]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MultimediaAppWidget.class), views);
        } else {
            imageCounter = imageCounter + 1;
            views.setImageViewResource(R.id.image_sail_1, IMAGES_ARRAY[imageCounter]);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MultimediaAppWidget.class), views);
        }
    }
}

