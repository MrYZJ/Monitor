package leavesc.hello.monitor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import leavesc.hello.monitor.db.MonitorHttpInformationDatabase;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/9 13:28
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorViewModel extends AndroidViewModel {

    private LiveData<List<MonitorHttpInformation>> recordLiveData;

    public MonitorViewModel(@NonNull Application application) {
        super(application);
        recordLiveData = MonitorHttpInformationDatabase.getInstance(application).getHttpInformationDao().queryAllRecordObservable();
    }

    public LiveData<List<MonitorHttpInformation>> getRecordLiveData() {
        return recordLiveData;
    }

}
