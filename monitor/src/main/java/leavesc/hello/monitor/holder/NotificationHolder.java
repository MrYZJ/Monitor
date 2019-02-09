package leavesc.hello.monitor.holder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.LongSparseArray;

import leavesc.hello.monitor.service.ClearMonitorService;
import leavesc.hello.monitor.MonitorActivity;
import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/8 20:59
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class NotificationHolder {

    private static final String CHANNEL_ID = "monitorLeavesChannelId";

    private static final String CHANNEL_NAME = "Http Notifications";

    private static final String NOTIFICATION_TITLE = "Recording Http Activity";

    private static final int NOTIFICATION_ID = 19950724;

    private static final int BUFFER_SIZE = 10;

    private LongSparseArray<MonitorHttpInformation> transactionBuffer = new LongSparseArray<>();

    private Context context;

    private NotificationManager notificationManager;

    private int transactionCount;

    private static volatile NotificationHolder instance;

    private NotificationHolder(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    public static NotificationHolder getInstance(Context context) {
        if (instance == null) {
            synchronized (NotificationHolder.class) {
                if (instance == null) {
                    instance = new NotificationHolder(context);
                }
            }
        }
        return instance;
    }

    public synchronized void show(MonitorHttpInformation transaction) {
        addToBuffer(transaction);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(getContentIntent(context))
                .setLocalOnly(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.chuck_colorPrimary))
                .setContentTitle(NOTIFICATION_TITLE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        int size = transactionBuffer.size();
        if (size > 0) {
            builder.setContentText(transactionBuffer.valueAt(size - 1).getNotificationText());
            for (int i = size - 1; i >= 0; i--) {
                inboxStyle.addLine(transactionBuffer.valueAt(i).getNotificationText());
            }
        }
        builder.setAutoCancel(true);
        builder.setStyle(inboxStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setSubText(String.valueOf(transactionCount));
        } else {
            builder.setNumber(transactionCount);
        }
        builder.addAction(getClearAction());
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private synchronized void addToBuffer(MonitorHttpInformation httpInformation) {
        transactionCount++;
        transactionBuffer.put(httpInformation.getId(), httpInformation);
        if (transactionBuffer.size() > BUFFER_SIZE) {
            transactionBuffer.removeAt(0);
        }
    }

    public synchronized void clearBuffer() {
        transactionBuffer.clear();
        transactionCount = 0;
    }

    public synchronized void dismiss() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent getContentIntent(Context context) {
        Intent intent = new Intent(context, MonitorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 100, intent, 0);
    }

    private NotificationCompat.Action getClearAction() {
        PendingIntent intent = PendingIntent.getService(context, 200,
                new Intent(context, ClearMonitorService.class), PendingIntent.FLAG_ONE_SHOT);
        return new NotificationCompat.Action(R.drawable.ic_launcher, "Clear", intent);
    }

}