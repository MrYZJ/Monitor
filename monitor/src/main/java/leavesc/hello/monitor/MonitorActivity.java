package leavesc.hello.monitor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import leavesc.hello.monitor.adapter.MonitorAdapter;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;

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

        MonitorViewModel monitorViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MonitorViewModel(getApplication());
            }
        }).get(MonitorViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MonitorAdapter adapter = new MonitorAdapter(this);
        adapter.setClickListener(new MonitorAdapter.OnClickListener() {
            @Override
            public void onClick(int position, MonitorHttpInformation model) {
                Toast.makeText(MonitorActivity.this, model.getId() + "", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        monitorViewModel.getRecordLiveData().observe(this, new Observer<List<MonitorHttpInformation>>() {
            @Override
            public void onChanged(@Nullable List<MonitorHttpInformation> monitorHttpInformationList) {
                adapter.setData(monitorHttpInformationList);
            }
        });
    }

}
