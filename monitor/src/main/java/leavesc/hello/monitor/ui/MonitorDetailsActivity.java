package leavesc.hello.monitor.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/9 16:47
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorDetailsActivity extends AppCompatActivity {

    private MonitorViewModel monitorViewModel;

    private static final String KEY_ID = "keyId";

    public static void navTo(Context context, long id) {
        Intent intent = new Intent(context, MonitorDetailsActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_details);
        initView();
        initViewModel();
        long id = getIntent().getLongExtra(KEY_ID, 0);
        monitorViewModel.queryRecordById(id);
        monitorViewModel.getRecordLiveData().observe(this, new Observer<MonitorHttpInformation>() {
            @Override
            public void onChanged(@Nullable MonitorHttpInformation monitorHttpInformation) {
                if (monitorHttpInformation != null) {
                    title.setText(String.format("%s %s", monitorHttpInformation.getMethod(), monitorHttpInformation.getPath()));
                } else {
                    title.setText(null);
                }
            }
        });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        title = findViewById(R.id.toolbar_title);
        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(MonitorOverviewFragment.newInstance(), "overview");
        fragmentPagerAdapter.addFragment(MonitorPayloadFragment.newInstanceRequest(), "request");
        fragmentPagerAdapter.addFragment(MonitorPayloadFragment.newInstanceResponse(), "response");
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        private List<String> fragmentTitleList = new ArrayList<>();

        private PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    private void initViewModel() {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);
    }

}