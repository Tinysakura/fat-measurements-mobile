package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;

public class SettingActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TextView settingLogOutTextView;

    private TitleLayout customTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
    }

    private void initUI() {
        customTitle = findViewById(R.id.setting_custom_title);
        customTitle.setTitle("设置");
        settingLogOutTextView = findViewById(R.id.setting_log_out);

        initSettingLogOutTextViewListener();
    }

    private void initSettingLogOutTextViewListener() {
        settingLogOutTextView.setOnClickListener(v -> {
            logOut();
        });
    }

    private void logOut() {
        SharedPreferences.Editor userDataEditor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
        userDataEditor.clear();
        userDataEditor.apply();

        getApp().initUserInfo();
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
