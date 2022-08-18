package com.rax.iaam.Others;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.R;



public class FCM extends FirebaseMessagingService {
    private static final String TAG = "FCM";
    static String ChannelID = "IAAMCHANNEL";
    public static final String ACTION_FCM = "FCM_RESPONSE";
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ChannelID);


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Log.d(TAG, remoteMessage.getData().toString());
            try {
                if (remoteMessage.getData().get("TotalSiteCount") != null) {
                    int count = Integer.parseInt(remoteMessage.getData().get("TotalSiteCount"));
                 //   assetDashModel.totalCount.set(count);
                }
                if (remoteMessage.getData().get("CommissionedSiteCount") != null) {
                    int count = Integer.parseInt(remoteMessage.getData().get("CommissionedSiteCount"));
                   // assetDashModel.commissionedCount.set(count);
                }
                if (remoteMessage.getData().get("ActiveSiteCount") != null) {
                    int count = Integer.parseInt(remoteMessage.getData().get("ActiveSiteCount"));
                    //poweredOnCount = poweredOnCount + (count);
                  /*  if (poweredOnCount < 0) {
                        assetDashModel.poweredOnCount
                                .set(0);
                    } else {
                        assetDashModel.poweredOnCount.set(poweredOnCount);
                    }*/
                }
                if (remoteMessage.getData().get("RunningSiteCount") != null) {
                    int count = Integer.parseInt(remoteMessage.getData().get("RunningSiteCount"));
                   /* runningCount = runningCount + (count);
                    if (runningCount < 0) {
                        assetDashModel.runningCount
                                .set(0);
                    } else {
                        assetDashModel.runningCount.set(runningCount);
                    }*/
                }
               /* if (remoteMessage.getData().get("PoweredOnCount") != null) {
                    clockTimeModel.poweredOnPercent.set(remoteMessage.getData().get("PoweredOnCount") + "%");
                }
                if (remoteMessage.getData().get("RunningCount") != null) {
                    clockTimeModel.operatedPercent.set(remoteMessage.getData().get("RunningCount") + "%");
                }
                if (remoteMessage.getData().get("IdleCount") != null) {
                    clockTimeModel.idlePercent
                            .set(remoteMessage.getData().get("IdleCount") + "%");
                }*/
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                if (remoteMessage.getData().containsKey("TelShapValue")) {
                    String value = remoteMessage.getData().get("TelShapValue");
                    String[] values = value.split("#");

                    String temperature = values[0].substring(1, values[0].length() - 1) + "." + values[0].substring(values[0].length() - 1);
                    double d = Double.parseDouble(temperature);
                 /*   ambienceData.temperature.set(d + " °C");

                    int i = Integer.parseInt(values[1].substring(1));
                    ambienceData.light.set(i + " lx");

                    int j = Integer.parseInt(values[2].substring(1));
                    ambienceData.sound.set(j + " dB");

                    int k = Integer.parseInt(values[3].substring(1));
                    ambienceData.humidity.set(k + " %");

                    int l = Integer.parseInt(values[4].substring(1));
                    ambienceData.airQuality.set(l + " %");

                    int m = Integer.parseInt(values[6].substring(1));
                    ambienceData.pressure.set(m + " Pa");

                    if (values[5].substring(1).equals("0")) {
                        ambienceData.motion.set("Normal");
                    } else {
                        ambienceData.motion.set("Motion detected");
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (remoteMessage.getData().containsKey("title") &&
                        remoteMessage.getData().containsKey("MachineSapId") &&
                        remoteMessage.getData().containsKey("AlertOn") &&
                        remoteMessage.getData().containsKey("message")
                ) {
                    Intent intentResponse = new Intent();
                    intentResponse.setAction(ACTION_FCM);
                    intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                    intentResponse.putExtra("fcm", "message");
                    sendBroadcast(intentResponse);
                    showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("openedFrom","Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        notificationBuilder.setContentTitle(title);
        // notificationBuilder.setContentText(message);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "IAAM";
            String description = "IoT Based Asset Tracking & Asset Management\n";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ChannelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
    }
}
