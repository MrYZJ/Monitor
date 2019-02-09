package leavesc.hello.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import leavesc.hello.monitor.Monitor;
import leavesc.hello.monitor.MonitorInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：leavesC
 * 时间：2019/2/8 20:53
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClient();
        findViewById(R.id.btnDoHttp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttpActivity();
            }
        });
        findViewById(R.id.btnDoHttp2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttpActivity2();
            }
        });
        findViewById(R.id.btnLaunchMonitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Monitor.getLaunchIntent(MainActivity.this));
            }
        });
    }

    private void initClient() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MonitorInterceptor(this))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private void doHttpActivity() {
        SampleApiService.HttpApi_1 api = SampleApiService.getInstance_1(okHttpClient);
        Callback<Void> cb = new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
            }

            @Override
            public void onFailure(@NonNull Call call, Throwable t) {
                t.printStackTrace();
            }
        };
        api.get().enqueue(cb);
        api.post(new SampleApiService.Data("posted")).enqueue(cb);
        api.patch(new SampleApiService.Data("patched")).enqueue(cb);
        api.put(new SampleApiService.Data("put")).enqueue(cb);
        api.delete().enqueue(cb);
        api.status(201).enqueue(cb);
        api.status(401).enqueue(cb);
        api.status(500).enqueue(cb);
        api.delay(9).enqueue(cb);
        api.delay(15).enqueue(cb);
        api.redirectTo("https://http2.akamai.com").enqueue(cb);
        api.redirect(3).enqueue(cb);
        api.redirectRelative(2).enqueue(cb);
        api.redirectAbsolute(4).enqueue(cb);
        api.stream(500).enqueue(cb);
        api.streamBytes(2048).enqueue(cb);
        api.image("image/png").enqueue(cb);
        api.gzip().enqueue(cb);
        api.xml().enqueue(cb);
        api.utf8().enqueue(cb);
        api.deflate().enqueue(cb);
        api.cookieSet("v").enqueue(cb);
        api.basicAuth("me", "pass").enqueue(cb);
        api.drip(512, 5, 1, 200).enqueue(cb);
        api.deny().enqueue(cb);
        api.cache("Mon").enqueue(cb);
        api.cache(30).enqueue(cb);
    }

    private void doHttpActivity2() {
        SampleApiService.HttpApi_2 api = SampleApiService.getInstance_2(okHttpClient);
        Callback<String> cb = new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
            }

            @Override
            public void onFailure(@NonNull Call call, Throwable t) {
                t.printStackTrace();
            }
        };
        api.singlePoetry().enqueue(cb);
        api.recommendPoetry().enqueue(cb);
        api.musicBroadcasting().enqueue(cb);
        api.novelApi().enqueue(cb);
    }

}