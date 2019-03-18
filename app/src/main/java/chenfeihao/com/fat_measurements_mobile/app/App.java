package chenfeihao.com.fat_measurements_mobile.app;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import chenfeihao.com.fat_measurements_mobile.activity.LoginActivity;
import chenfeihao.com.fat_measurements_mobile.constant.enums.CommonCookieKeyEnum;
import chenfeihao.com.fat_measurements_mobile.http.constant.CommonConstant;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 使用自定义的Application
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/18
 */
public class App extends Application {
    private String userId;

    private Retrofit retrofit;

    private MobileUser mobileUser;

    @Override
    public void onCreate() {
        super.onCreate();

        initUserInfo();
        if (mobileUser != null) {
            initRetrofitWithUserInfo(mobileUser);
        } else {
            initRetrofit();
        }
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.ServiceHostConstant.host)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void initRetrofitWithUserInfo(MobileUser userInfo) {
        /**
         * 使用OkHttp过滤器统一为请求头添加用户身份标识
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder().addHeader(CommonCookieKeyEnum.LOGIN_ID.getValue(), userInfo.getUserName()).build();
            return chain.proceed(newRequest);
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.ServiceHostConstant.host)
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    private void initUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String mobileUserJsonStr = sharedPreferences.getString("mobile_user", null);
        MobileUser mobileUser = JSON.parseObject(mobileUserJsonStr, MobileUser.class);

        /**
         * 如果没有用户信息或用户信息过期则跳转到LoginActivity
         */
        if (StringUtil.isEmpty(mobileUserJsonStr) || mobileUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (mobileUser.getValidTime().compareTo(System.currentTimeMillis()) < 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {// 否则将用户信息塞入全局变量中
            this.mobileUser = mobileUser;
        }

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }
}
