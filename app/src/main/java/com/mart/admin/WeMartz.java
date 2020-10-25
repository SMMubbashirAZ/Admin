package com.mart.admin;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import com.mart.admin.utils.Constants;

import java.util.Locale;

/**
 * Created by WeMartDevelopers .
 */
public class WeMartz extends Application {

    private static final String TAG = WeMartz.class.getSimpleName();
    private static WeMartz mInstance;

    private Locale mSystemLocale;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mSystemLocale = Locale.getDefault();




    }

    public static synchronized WeMartz getInstance() {
        return mInstance;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription("Simple Android Notification Channel For Show Notification in Android Q and Greater");
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableLights(true);
            channel.setLightColor(2184028);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
