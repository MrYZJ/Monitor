package leavesc.hello.monitor.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/9 16:49
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorOverviewFragment extends Fragment {

    private TextView url;
    private TextView method;
    private TextView protocol;
    private TextView status;
    private TextView response;
    private TextView ssl;
    private TextView requestTime;
    private TextView responseTime;
    private TextView duration;
    private TextView requestSize;
    private TextView responseSize;
    private TextView totalSize;

    private static final String KEY_ID = "keyId";

    public static MonitorOverviewFragment newInstance(long id) {
        MonitorOverviewFragment fragment = new MonitorOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MonitorOverviewFragment newInstance() {
        return new MonitorOverviewFragment();
    }

    public MonitorOverviewFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_overview, container, false);
        url = view.findViewById(R.id.url);
        method = view.findViewById(R.id.method);
        protocol = view.findViewById(R.id.protocol);
        status = view.findViewById(R.id.status);
        response = view.findViewById(R.id.response);
        ssl = view.findViewById(R.id.ssl);
        requestTime = view.findViewById(R.id.request_time);
        responseTime = view.findViewById(R.id.response_time);
        duration = view.findViewById(R.id.duration);
        requestSize = view.findViewById(R.id.request_size);
        responseSize = view.findViewById(R.id.response_size);
        totalSize = view.findViewById(R.id.total_size);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MonitorViewModel monitorViewModel = ViewModelProviders.of(getActivity()).get(MonitorViewModel.class);
        monitorViewModel.getRecordLiveData().observe(this, new Observer<MonitorHttpInformation>() {
            @Override
            public void onChanged(@Nullable MonitorHttpInformation monitorHttpInformation) {
                if (monitorHttpInformation != null) {
                    url.setText(monitorHttpInformation.getUrl());
                    method.setText(monitorHttpInformation.getMethod());
                    protocol.setText(monitorHttpInformation.getProtocol());
                    status.setText(monitorHttpInformation.getStatus().toString());
                    response.setText(monitorHttpInformation.getResponseSummaryText());
                    ssl.setText((monitorHttpInformation.isSsl() ? "Yes" : "No"));
                    requestTime.setText(monitorHttpInformation.getRequestDateFormat());
                    responseTime.setText(monitorHttpInformation.getResponseDateFormat());
                    duration.setText(monitorHttpInformation.getDurationFormat());
                    requestSize.setText(String.valueOf(monitorHttpInformation.getRequestContentLength()));
                    responseSize.setText(String.valueOf(monitorHttpInformation.getResponseContentLength()));
                    totalSize.setText(monitorHttpInformation.getTotalSizeString());
                }
            }
        });
    }

}