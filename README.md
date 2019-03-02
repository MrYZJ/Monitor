# Monitor

[ ![Download](https://api.bintray.com/packages/leavesc/MonitorRepo/Monitor/images/download.svg?version=1.0.4) ](https://bintray.com/leavesc/MonitorRepo/Monitor/1.0.4/link)


使用方法：


在 **build.gradle** 文件中添加依赖：

```
    implementation 'leavesc.hello:Monitor:1.0.3'
```

添加 **MonitorInterceptor** 作为项目中 **OkHttpClient** 的拦截器

```
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new MonitorInterceptor(this)).build();
```

**然后？没了，OK了**





> 以下内容发布于 2019-03-02

修改了 HttpInformation 的 equals 方法实现方式，修复低版本的兼容性问题，并调整了下 UI，发布 v1.0.4 版本





> 以下内容发布于 2019-02-10

### 一、概述

**Monitor** 是我刚开发完成的一个开源项目，适用于使用了 OkHttp 作为网络请求框架的项目，可以拦截并缓存应用内的所有 Http 请求和响应信息，且可以以 Notification 和 Activity 的形式来展示具体内容

![](https://upload-images.jianshu.io/upload_images/2552605-590161bfc9f353d2.gif?imageMogr2/auto-orient/strip)

![](https://upload-images.jianshu.io/upload_images/2552605-043084a61be063fb.gif?imageMogr2/auto-orient/strip)

### 二、使用

项目主页：[Android OkHttp 网络请求调试利器 - Monitor](https://github.com/leavesC/Monitor)

Apk下载：[Android OkHttp 网络请求调试利器 - Monitor](https://www.pgyer.com/leavesC_Monitor)

在 **build.gradle** 文件中添加依赖：

```
    implementation 'leavesc.hello:Monitor:1.0.3'
```

添加 **MonitorInterceptor** 作为项目中 **OkHttpClient** 的拦截器

```
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new MonitorInterceptor(this)).build();
```

**然后？没了，OK了**

是的，就是这么简单，只要添加了 **MonitorInterceptor** 拦截器，之后 **Monitor** 就会自动记录下所有 **Http 请求的请求信息以及响应体**，且自动弹窗提示。当然，为了照顾到其他一些特殊情况，**Monitor** 也对外提供了一些方便访问的 Api

**注意：以下方法需要在实例化 MonitorInterceptor 后再调用，否则会抛出异常**

 **1. 启动 Http 列表页**

```
    startActivity(Monitor.getLaunchIntent(MainActivity.this));
```

 **2. 开启弹窗**

```
    Monitor.showNotification(true);
```

 **3. 关闭弹窗（当有新数据时也不会显示）**

```
    Monitor.showNotification(false);
```

 **4. 清除弹窗（当有新数据时会再次显示）**

```
    Monitor.clearNotification();
```

 **5. 清除缓存**

```
    Monitor.clearNotification();
```

 **6. 监听 Http 数据变化**

```
        //参数用于监听最新指定条数的数据变化，如果不传递参数则会监听所有的数据变化
        Monitor.queryAllRecord(10).observe(this, new Observer<List<HttpInformation>>() {
            @Override
            public void onChanged(@Nullable List<HttpInformation> httpInformationList) {
                tv_log.setText(null);
                if (httpInformationList != null) {
                    for (HttpInformation httpInformation : httpInformationList) {
                        tv_log.append(httpInformation.toString());
                        tv_log.append("\n\n");
                        tv_log.append("*************************************");
                        tv_log.append("\n\n");
                    }
                }
            }
        });
```

### 三、致谢

Monitor 的一部分灵感来源于另一个开源项目：[Chuck](https://github.com/jgilfelt/chuck)，因此你可以看到两个项目的 UI 基本是相同的，因为我觉得 UI 是次要的，也懒得去想新的交互方式，我借鉴的主要是其拦截器的数据抓取思路。而因为我对 Chuck 有些地方不太满意，包括**Notification 无法动态精确控制、无法通过 API 清除缓存、无法监听数据变化**等，所以才打算自己来实现

此外，Monitor 使用到的依赖还包括：

```
    implementation "com.squareup.okhttp3:okhttp:3.12.0"
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'android.arch.persistence.room:runtime:1.1.1'
    annotationProcessor 'android.arch.persistence.room:compiler:1.1.1'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
```

当中，okhttp 和 gson 不必说，room 和 lifecycle 都是 Google Jetpack 组件的一部分，room 和 lifecycle 搭配使用真的还是蛮爽的~~

### 四、结束语

项目主页：[Android OkHttp 网络请求调试利器 - Monitor](https://github.com/leavesC/Monitor)

Apk下载：[Android OkHttp 网络请求调试利器 - Monitor](https://www.pgyer.com/leavesC_Monitor)

欢迎 star
