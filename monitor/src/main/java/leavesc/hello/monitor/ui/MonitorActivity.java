package leavesc.hello.monitor.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.adapter.MonitorAdapter;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/8 23:00
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorActivity extends AppCompatActivity {

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
            public void onClick(int position, HttpInformation model) {
                MonitorDetailsActivity.navTo(MonitorActivity.this, model.getId());
            }
        });
        recyclerView.setAdapter(adapter);
        monitorViewModel.getAllRecordLiveData().observe(this, new Observer<List<HttpInformation>>() {
            @Override
            public void onChanged(@Nullable List<HttpInformation> HttpInformationList) {
                adapter.setData(HttpInformationList);
            }
        });
    }

    private void initViewModel() {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView tvTitle = findViewById(R.id.tvToolbarTitle);
        tvTitle.setText("Monitor");
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
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
