package leavesc.hello.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import leavesc.hello.monitor.Monitor
import leavesc.hello.monitor.MonitorInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 作者：leavesC
 * 时间：2019/11/7 17:01
 * 描述：
 */
class MainActivity : AppCompatActivity() {

    private lateinit var okHttpClient: OkHttpClient

    private val clickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.btnDoHttp -> {
                    doHttpActivity()
                }
                R.id.btnDoHttp2 -> {
                    doHttpActivity2()
                }
                R.id.btnLaunchMonitor -> {
                    startActivity(Monitor.getLaunchIntent(this@MainActivity))
                }
                R.id.btnOpenNotification -> {
                    Monitor.showNotification(true)
                }
                R.id.btnCloseNotification -> {
                    Monitor.showNotification(false)
                    Monitor.clearNotification()
                }
                R.id.btnClearNotification -> {
                    run { Monitor.clearCache() }
                    run { Thread(Runnable { Monitor.clearCache() }).start() }
                }
                R.id.btnClearCache -> {
                    Thread(Runnable { Monitor.clearCache() }).start()
                }
            }
        }
    }

    private lateinit var tv_log: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClient()
        findViewById<View>(R.id.btnDoHttp).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnDoHttp2).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnLaunchMonitor).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnOpenNotification).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnCloseNotification).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnClearNotification).setOnClickListener(clickListener)
        findViewById<View>(R.id.btnClearCache).setOnClickListener(clickListener)
        tv_log = findViewById(R.id.tv_log)
        //参数用于监听最新指定条数的数据变化，如果不传递参数则会监听所有的数据变化
        Monitor.queryAllRecord(10).observe(this, Observer { httpInformationList ->
            tv_log.text = null
            if (httpInformationList != null) {
                for (httpInformation in httpInformationList) {
                    tv_log.append(httpInformation.toString())
                    tv_log.append("\n\n")
                    tv_log.append("*************************************")
                    tv_log.append("\n\n")
                }
            }
        })
    }

    private fun initClient() {
        //MonitorInterceptor 必须先初始化后才可以调用 Monitor 中的方法
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(MonitorInterceptor(this))
                .addInterceptor(httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
    }

    private fun doHttpActivity() {
        val api = SampleApiService.getInstance_1(okHttpClient)
        val cb = object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

            }
        }
        api.get().enqueue(cb)
        api.post(SampleApiService.Data("posted")).enqueue(cb)
        api.patch(SampleApiService.Data("patched")).enqueue(cb)
        api.put(SampleApiService.Data("put")).enqueue(cb)
        api.delete().enqueue(cb)
        api.status(201).enqueue(cb)
        api.status(401).enqueue(cb)
        api.status(500).enqueue(cb)
        api.delay(9).enqueue(cb)
        api.delay(15).enqueue(cb)
        api.redirectTo("https://http2.akamai.com").enqueue(cb)
        api.redirect(3).enqueue(cb)
        api.redirectRelative(2).enqueue(cb)
        api.redirectAbsolute(4).enqueue(cb)
        api.stream(500).enqueue(cb)
        api.streamBytes(2048).enqueue(cb)
        api.image("image/png").enqueue(cb)
        api.gzip().enqueue(cb)
        api.xml().enqueue(cb)
        api.utf8().enqueue(cb)
        api.deflate().enqueue(cb)
        api.cookieSet("v").enqueue(cb)
        api.basicAuth("me", "pass").enqueue(cb)
        api.drip(512, 5, 1, 200).enqueue(cb)
        api.deny().enqueue(cb)
        api.cache("Mon").enqueue(cb)
        api.cache(30).enqueue(cb)
    }

    private fun doHttpActivity2() {
        val api = SampleApiService.getInstance_2(okHttpClient)
        val cb = object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

            }

        }
        api.singlePoetry().enqueue(cb)
        api.recommendPoetry().enqueue(cb)
        api.musicBroadcasting().enqueue(cb)
        api.novelApi().enqueue(cb)
    }

}