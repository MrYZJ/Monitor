package leavesc.hello.sample;

import android.app.Application;

/**
 * 作者：leavesC
 * 时间：2019/2/8 21:05
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class BaseApplication extends Application {

    private static Application appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static Application getAppContext() {
        return appContext;
    }

}