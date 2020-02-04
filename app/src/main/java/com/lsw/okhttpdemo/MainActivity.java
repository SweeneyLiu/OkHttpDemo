package com.lsw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import com.lsw.lighthttp.CommonOkHttpClient;
//import com.lsw.lighthttp.exception.OkHttpException;
//import com.lsw.lighthttp.listener.DisposeDataHandle;
//import com.lsw.lighthttp.listener.DisposeDataListener;
//import com.lsw.lighthttp.request.CommonRequest;

import com.lsw.okhttpdemo.interceptor.LogInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lsw---MainActivity";

    public static final String URL = "https://publicobject.com/helloworld.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getHttp();
//        get();
//        getLightOkHttp();
        testOkHttpCache();
    }

    public void get(){
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url("http://www.baidu.com").build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
            }
        });
    }

    private void getHttp(){
        //1. 生成OkHttpClient实例对象'
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //配置拦截器
//        builder.addInterceptor(new LogInterceptor());
        //配置超时
//        builder.connectTimeout(15, TimeUnit.SECONDS);
//        builder.writeTimeout(20,TimeUnit.SECONDS);
//        builder.readTimeout(20,TimeUnit.SECONDS);
        //配置缓存
        //缓存文件夹
//        File cacheFile = new File(getExternalCacheDir().toString(),"cache");
        //缓存大小为10M
//        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
//        Cache cache = new Cache(cacheFile,cacheSize);
//        builder.cache(cache);

        OkHttpClient okHttpClient = builder.build();
        //2. 生成Request对象'
        Request request = new Request
                .Builder()
                .url(URL)
                .build();
        //3. 生成Call对象'
        Call call = okHttpClient.newCall(request);
        //4. 如果要执行同步请求：'
//        try {
//            call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //5. 如果要执行异步请求：'
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    private void testOkHttpCache(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //配置缓存
        //缓存文件夹
        File cacheFile = new File(getExternalCacheDir().toString(),"cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
        Cache cache = new Cache(cacheFile,cacheSize);
        builder.cache(cache);

        OkHttpClient client = builder.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(URL).cacheControl(CacheControl.FORCE_CACHE);
        Request request = requestBuilder.build();


        final Call call = client.newCall(request);

        final Call call12 = client.newCall(request);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response1 = null;
                try {
                    response1 = call.execute();
                    Log.i(TAG, "testCache: response1 :"+response1);
                    Log.i(TAG, "testCache: response1 body:"+response1.body().string());
                    Log.i(TAG, "testCache: response1 cache :"+response1.cacheResponse());
                    Log.i(TAG, "testCache: response1 network :"+response1.networkResponse());
                    response1.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    //第二次网络请求
                    Response response2 = call12.execute();
                    Log.i(TAG, "testCache: response2 :"+response2.body().string());
                    Log.i(TAG, "testCache: response2 cache :"+response2.cacheResponse());
                    Log.i(TAG, "testCache: response2 network :"+response2.networkResponse());
                    Log.i(TAG, "testCache: response1 equals response2:"+response2.equals(response1));
                    response2.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void testCacheControl(){
        //缓存文件夹
        File cacheFile = new File(getExternalCacheDir().toString(),"cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
        final Cache cache = new Cache(cacheFile,cacheSize);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .cache(cache)
                        .build();
                //设置缓存时间为60秒
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(60, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://blog.csdn.net/briblue")
                        .cacheControl(cacheControl)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
