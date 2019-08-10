package leavesc.hello.monitor;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import leavesc.hello.monitor.db.MonitorHttpInformationDatabase;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.holder.ContextHolder;
import leavesc.hello.monitor.holder.NotificationHolder;
import leavesc.hello.monitor.ui.MonitorActivity;

/**
 * 作者：leavesC
 * 时间：2019/2/9 19:34
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class Monitor {

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, MonitorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void clearNotification() {
        NotificationHolder.getInstance(ContextHolder.getContext()).clearBuffer();
        NotificationHolder.getInstance(ContextHolder.getContext()).dismiss();
    }

    public static void showNotification(boolean showNotification) {
        NotificationHolder.getInstance(ContextHolder.getContext()).showNotification(showNotification);
    }

    public static void clearCache() {
        MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().deleteAll();
    }

    public static LiveData<List<HttpInformation>> queryAllRecord(int limit) {
        return MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().queryAllRecordObservable(limit);
    }

    public static LiveData<List<HttpInformation>> queryAllRecord() {
        return MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().queryAllRecordObservable();
    }

}