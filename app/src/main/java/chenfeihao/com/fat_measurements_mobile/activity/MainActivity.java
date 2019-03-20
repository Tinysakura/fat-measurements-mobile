package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.UserInformationConstant;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * UI
     */
    private NavigationView navigationView;

    private View navHeaderView;

    private CircleImageView navHeadPortraitCircleImageView;

    private TextView navUserNameTextView;

    private TextView navUserSignatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI(){
        navigationView = findViewById(R.id.nav_view);
        // 设置menu中的data item被默认选中
        navigationView.setCheckedItem(R.id.nav_data);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        navHeaderView = inflater.inflate(R.layout.layout_nav_header, null);

        navHeadPortraitCircleImageView = navHeaderView.findViewById(R.id.nav_head_portrait);
        navUserNameTextView = navHeaderView.findViewById(R.id.nav_user_name);
        navUserSignatureTextView = navHeaderView.findViewById(R.id.nav_personal_signature);
        renderNavHeaderView();

        initNavigationViewListener();
    }

    /**
     * 渲染侧拉边栏头部信息
     */
    private void renderNavHeaderView() {
        MobileUser mobileUser = getApp().getMobileUser();
        navUserNameTextView.setText(mobileUser.getUserName());
        navUserSignatureTextView.setText(mobileUser.getSignature());

        if (!StringUtil.isEmpty(mobileUser.getUserHeadPortrait())) {
            Glide.with(this).load(mobileUser.getUserHeadPortrait()).into(navHeadPortraitCircleImageView);
        }
    }

    // 给menu中的item设置点击事件
    private void initNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_user:
                    Intent intent = new Intent(MainActivity.this, UserInformationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_data:
                    break;
                case R.id.nav_account:
                    break;
                case R.id.nav_setting:
                    break;
            }

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        jump2Login();

        renderNavHeaderView();
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
