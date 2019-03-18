package chenfeihao.com.fat_measurements_mobile.app;

import android.app.Application;

import chenfeihao.com.fat_measurements_mobile.http.constant.CommonConstant;
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

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.ServiceHostConstant.host)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
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
}
