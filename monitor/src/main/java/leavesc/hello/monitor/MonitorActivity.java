package leavesc.hello.monitor;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import leavesc.hello.monitor.database.MonitorHttpInformationDatabase;
import leavesc.hello.monitor.database.entity.MonitorHttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/8 23:00
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorActivity extends AppCompatActivity {

    private static final String TAG = "MonitorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        MonitorHttpInformationDatabase.getInstance(MonitorActivity.this).getHttpInformationDao().queryAllRecordObservable().observe(this, new Observer<List<MonitorHttpInformation>>() {
            @Override
            public void onChanged(@Nullable List<MonitorHttpInformation> monitorHttpInformationList) {
                Log.e(TAG, "*****************************");
                for (MonitorHttpInformation monitorHttpInformation : monitorHttpInformationList) {
                    Log.e(TAG, monitorHttpInformation.toString());
                }
                Log.e(TAG, "*****************************");
            }
        });
    }
}
