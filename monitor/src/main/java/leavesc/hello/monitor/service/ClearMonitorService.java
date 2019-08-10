package leavesc.hello.monitor.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import leavesc.hello.monitor.holder.NotificationHolder;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:05
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class ClearMonitorService extends IntentService {

    public ClearMonitorService() {
        super(ClearMonitorService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationHolder holder = NotificationHolder.getInstance(this);
        holder.clearBuffer();
        holder.dismiss();
    }

}