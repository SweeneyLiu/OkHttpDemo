package com.lsw.okhttpdemo.interceptor;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sweeneyliu on 2020/2/3.
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = "lsw---LogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //1.通过传进来的chain获取request
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(TAG, "request = " + request.toString());
        //处理request
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        //1e6d = 10的6的方
        Log.d(TAG, "time cost = " + (t2 - t1) / 1e6d + "ms \n response = " + response.toString());

        return response;
    }
}
