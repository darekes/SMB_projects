package pl.dsamsel.mp2.BroadcastReceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import pl.dsamsel.mp2.Activities.ModifyProductActivity;
import pl.dsamsel.mp2.R;
import pl.dsamsel.mp2.Services.DatabaseService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "dsamsel";
    private int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseService databaseService = new DatabaseService(context);
        databaseService.init();
        String productName = intent.getStringExtra("productName");
        int productPrice = intent.getIntExtra("productPrice", 0);
        int productQuantity = intent.getIntExtra("productQuantity", 0);
        boolean isProductBought = intent.getBooleanExtra("isProductBought", false);
        long productId = databaseService.insertProductAndReturnId(productName, productPrice, productQuantity, isProductBought);

        Intent startIntent = new Intent(context, ModifyProductActivity.class);
        startIntent.putExtra("productId", Math.toIntExact(productId));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Product added!")
                .setContentText("Name: " + productName + ", price: " + productPrice
                        + ", quantity: " + productQuantity + ", is bought: " + isProductBought)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                "notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(id++, notification.build());
    }
}
