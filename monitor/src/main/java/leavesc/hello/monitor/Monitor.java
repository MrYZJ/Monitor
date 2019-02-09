package leavesc.hello.monitor;

import android.content.Context;
import android.content.Intent;

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

}