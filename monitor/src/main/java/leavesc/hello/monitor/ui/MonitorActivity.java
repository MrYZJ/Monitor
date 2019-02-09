package leavesc.hello.monitor.ui;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.adapter.MonitorAdapter;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/8 23:00
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorActivity extends AppCompatActivity {

    private static final String TAG = "MonitorActivity";

    private MonitorViewModel monitorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        initView();
        initViewModel();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MonitorAdapter adapter = new MonitorAdapter(this);
        adapter.setClickListener(new MonitorAdapter.OnClickListener() {
            @Override
            public void onClick(int position, MonitorHttpInformation model) {
                MonitorDetailsActivity.navTo(MonitorActivity.this, model.getId());
            }
        });
        recyclerView.setAdapter(adapter);
        monitorViewModel.getAllRecordLiveData().observe(this, new Observer<List<MonitorHttpInformation>>() {
            @Override
            public void onChanged(@Nullable List<MonitorHttpInformation> monitorHttpInformationList) {
                adapter.setData(monitorHttpInformationList);
            }
        });
    }

    private void initViewModel() {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_monitor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            monitorViewModel.clearAllCache();
            monitorViewModel.clearNotification();
        }
        return true;
    }

}
