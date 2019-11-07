package leavesc.hello.monitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import leavesc.hello.monitor.R
import leavesc.hello.monitor.utils.FormatUtils
import leavesc.hello.monitor.viewmodel.MonitorViewModel

/**
 * 作者：leavesC
 * 时间：2019/11/7 15:19
 * 描述：
 */
class MonitorOverviewFragment : Fragment() {

    companion object {

        fun newInstance(): MonitorOverviewFragment {
            return MonitorOverviewFragment()
        }
    }

    private lateinit var tv_url: TextView
    private lateinit var tv_method: TextView
    private lateinit var tv_protocol: TextView
    private lateinit var tv_status: TextView
    private lateinit var tv_response: TextView
    private lateinit var tv_ssl: TextView
    private lateinit var tv_requestTime: TextView
    private lateinit var tv_responseTime: TextView
    private lateinit var tv_duration: TextView
    private lateinit var tv_requestSize: TextView
    private lateinit var tv_responseSize: TextView
    private lateinit var tv_totalSize: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitor_overview, container, false)
        tv_url = view.findViewById(R.id.tv_url)
        tv_method = view.findViewById(R.id.tv_method)
        tv_protocol = view.findViewById(R.id.tv_protocol)
        tv_status = view.findViewById(R.id.tv_status)
        tv_response = view.findViewById(R.id.tv_response)
        tv_ssl = view.findViewById(R.id.tv_ssl)
        tv_requestTime = view.findViewById(R.id.tv_request_time)
        tv_responseTime = view.findViewById(R.id.tv_response_time)
        tv_duration = view.findViewById(R.id.tv_duration)
        tv_requestSize = view.findViewById(R.id.tv_request_size)
        tv_responseSize = view.findViewById(R.id.tv_response_size)
        tv_totalSize = view.findViewById(R.id.tv_total_size)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val monitorViewModel = ViewModelProviders.of(activity!!).get(MonitorViewModel::class.java)
        monitorViewModel.recordLiveData?.observe(this, Observer { monitorHttpInformation ->
            monitorHttpInformation?.apply {
                tv_url.text = url
                tv_method.text = method
                tv_protocol.text = protocol
                tv_status.text = status.toString()
                tv_response.text = responseSummaryText
                tv_ssl.text = if (isSsl) "Yes" else "No"
                tv_requestTime.text = FormatUtils.getDateFormatLong(requestDate)
                tv_responseTime.text = FormatUtils.getDateFormatLong(responseDate)
                tv_duration.text = durationFormat
                tv_requestSize.text = FormatUtils.formatBytes(requestContentLength)
                tv_responseSize.text = FormatUtils.formatBytes(responseContentLength)
                tv_totalSize.text = totalSizeString
            }
        })
    }

}