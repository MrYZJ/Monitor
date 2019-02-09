package leavesc.hello.monitor.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import leavesc.hello.monitor.db.MonitorHttpInformationDatabase;
import leavesc.hello.monitor.holder.ContextHolder;
import leavesc.hello.monitor.holder.NotificationHolder;
import leavesc.hello.monitor.db.entity.HttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/9 13:28
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorViewModel extends ViewModel {

    private LiveData<List<HttpInformation>> allRecordLiveData;

    private LiveData<HttpInformation> recordLiveData;

    private static final int LIMIT = 300;

    public MonitorViewModel() {
        allRecordLiveData = MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().queryAllRecordObservable(LIMIT);
    }

    public void clearAllCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().deleteAll();
            }
        }).start();
    }

    public void clearNotification() {
        NotificationHolder.getInstance(ContextHolder.getContext()).dismiss();
    }

    public void queryRecordById(long id) {
        recordLiveData = MonitorHttpInformationDatabase.getInstance(ContextHolder.getContext()).getHttpInformationDao().queryRecordObservable(id);
    }

    public LiveData<List<HttpInformation>> getAllRecordLiveData() {
        return allRecordLiveData;
    }

    public LiveData<HttpInformation> getRecordLiveData() {
        return recordLiveData;
    }

}
