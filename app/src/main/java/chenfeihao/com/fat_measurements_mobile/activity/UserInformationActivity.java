package chenfeihao.com.fat_measurements_mobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;

public class UserInformationActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout cutomeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initUI();
    }

    private void initUI() {
        cutomeTitle = findViewById(R.id.user_custom_title);
        cutomeTitle.setTitle("编辑资料");
    }
}
