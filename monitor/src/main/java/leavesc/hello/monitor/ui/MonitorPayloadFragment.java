package leavesc.hello.monitor.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.viewmodel.MonitorViewModel;

/**
 * 作者：leavesC
 * 时间：2019/2/9 18:26
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorPayloadFragment extends Fragment {

    private static final int TYPE_REQUEST = 100;

    private static final int TYPE_RESPONSE = 200;

    private static final String TYPE_KEY = "keyType";

    private TextView tvHeaders;

    private TextView tvBody;

    private int type;

    private static MonitorPayloadFragment newInstance(int type) {
        MonitorPayloadFragment fragment = new MonitorPayloadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MonitorPayloadFragment newInstanceRequest() {
        return newInstance(TYPE_REQUEST);
    }

    public static MonitorPayloadFragment newInstanceResponse() {
        return newInstance(TYPE_RESPONSE);
    }

    public MonitorPayloadFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(TYPE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_payload, container, false);
        tvHeaders = view.findViewById(R.id.tvHeaders);
        tvBody = view.findViewById(R.id.tvBody);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        MonitorViewModel monitorViewModel = ViewModelProviders.of(getActivity()).get(MonitorViewModel.class);
        monitorViewModel.getRecordLiveData().observe(this, new Observer<HttpInformation>() {
            @Override
            public void onChanged(@Nullable HttpInformation monitorHttpInformation) {
                if (monitorHttpInformation != null) {
                    switch (type) {
                        case TYPE_REQUEST: {
                            setText(monitorHttpInformation.getRequestHeadersString(true),
                                    monitorHttpInformation.getFormattedRequestBody(), monitorHttpInformation.isRequestBodyIsPlainText());
                            break;
                        }
                        case TYPE_RESPONSE: {
                            setText(monitorHttpInformation.getResponseHeadersString(true),
                                    monitorHttpInformation.getFormattedResponseBody(), monitorHttpInformation.isResponseBodyIsPlainText());
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setText(String headersString, String bodyString, boolean isPlainText) {
        tvHeaders.setVisibility((TextUtils.isEmpty(headersString) ? View.GONE : View.VISIBLE));
        tvHeaders.setText(Html.fromHtml(headersString));
        if (!isPlainText) {
            tvBody.setText("(encoded or binary body omitted)");
        } else {
            tvBody.setText(bodyString);
        }
    }

}