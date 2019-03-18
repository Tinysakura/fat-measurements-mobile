package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jump2Login();
    }

    private void jump2Login() {
        if (getApp().getMobileUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
