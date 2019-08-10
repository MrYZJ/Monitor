package leavesc.hello.monitor.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.utils.FormatUtils;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/9 16:49
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorOverviewFragment extends Fragment {

    private TextView tv_url;
    private TextView tv_method;
    private TextView tv_protocol;
    private TextView tv_status;
    private TextView tv_response;
    private TextView tv_ssl;
    private TextView tv_requestTime;
    private TextView tv_responseTime;
    private TextView tv_duration;
    private TextView tv_requestSize;
    private TextView tv_responseSize;
    private TextView tv_totalSize;

    public static MonitorOverviewFragment newInstance() {
        return new MonitorOverviewFragment();
    }

    public MonitorOverviewFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_overview, container, false);
        tv_url = view.findViewById(R.id.tv_url);
        tv_method = view.findViewById(R.id.tv_method);
        tv_protocol = view.findViewById(R.id.tv_protocol);
        tv_status = view.findViewById(R.id.tv_status);
        tv_response = view.findViewById(R.id.tv_response);
        tv_ssl = view.findViewById(R.id.tv_ssl);
        tv_requestTime = view.findViewById(R.id.tv_request_time);
        tv_responseTime = view.findViewById(R.id.tv_response_time);
        tv_duration = view.findViewById(R.id.tv_duration);
        tv_requestSize = view.findViewById(R.id.tv_request_size);
        tv_responseSize = view.findViewById(R.id.tv_response_size);
        tv_totalSize = view.findViewById(R.id.tv_total_size);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MonitorViewModel monitorViewModel = ViewModelProviders.of(getActivity()).get(MonitorViewModel.class);
        monitorViewModel.getRecordLiveData().observe(this, new Observer<HttpInformation>() {
            @Override
            public void onChanged(@Nullable HttpInformation monitorHttpInformation) {
                if (monitorHttpInformation != null) {
                    tv_url.setText(monitorHttpInformation.getUrl());
                    tv_method.setText(monitorHttpInformation.getMethod());
                    tv_protocol.setText(monitorHttpInformation.getProtocol());
                    tv_status.setText(monitorHttpInformation.getStatus().toString());
                    tv_response.setText(monitorHttpInformation.getResponseSummaryText());
                    tv_ssl.setText((monitorHttpInformation.isSsl() ? "Yes" : "No"));
                    tv_requestTime.setText(FormatUtils.getDateFormatLong(monitorHttpInformation.getRequestDate()));
                    tv_responseTime.setText(FormatUtils.getDateFormatLong(monitorHttpInformation.getResponseDate()));
                    tv_duration.setText(monitorHttpInformation.getDurationFormat());
                    tv_requestSize.setText(FormatUtils.formatBytes(monitorHttpInformation.getRequestContentLength()));
                    tv_responseSize.setText(FormatUtils.formatBytes(monitorHttpInformation.getResponseContentLength()));
                    tv_totalSize.setText(monitorHttpInformation.getTotalSizeString());
                }
            }
        });
    }

}