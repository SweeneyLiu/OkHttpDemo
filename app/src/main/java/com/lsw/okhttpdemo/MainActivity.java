package com.lsw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lsw.lighthttp.CommonOkHttpClient;
import com.lsw.lighthttp.exception.OkHttpException;
import com.lsw.lighthttp.listener.DisposeDataHandle;
import com.lsw.lighthttp.listener.DisposeDataListener;
import com.lsw.lighthttp.request.CommonRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get();
        getLightOkHttp();
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

    public void getLightOkHttp(){
        Request request = CommonRequest.createGetRequest("http://www.baidu.com",null);
        CommonOkHttpClient.get(request,new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object o) {
                Log.i(TAG, "getLightOkHttp--onSuccess: ");
            }

            @Override
            public void onFailure(Object o) {
                OkHttpException httpException = (OkHttpException)o;
                Log.i(TAG, "getLightOkHttp--onFailure: msg = " + httpException.getEmsg() + ";code = " + httpException.getEcode());
            }
        }));
    }

}
