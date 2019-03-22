package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.List;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
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

    private NiceSpinner publishTimeSortNiceSpinner;

    private NiceSpinner varietyFilterNiceSpinner;

    private SwipeRefreshLayout swipeRefreshLayout;

    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI(){
        navigationView = findViewById(R.id.nav_view);
        publishTimeSortNiceSpinner = findViewById(R.id.publish_time_sort);
        varietyFilterNiceSpinner = findViewById(R.id.variety_filter);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        bottomNavigationBar = findViewById(R.id.main_bottom_bar);

        initNavigationView();
        initBottomNavigationBar();
        initNiceSpinner();
    }

    private void initNavigationView() {
        // 设置menu中的data item被默认选中
        navigationView.setCheckedItem(R.id.nav_data);

        navHeaderView = navigationView.getHeaderView(0);

        if (navHeaderView == null) {
            navHeaderView = navigationView.inflateHeaderView(R.layout.layout_nav_header);
        }

        navHeadPortraitCircleImageView = navHeaderView.findViewById(R.id.nav_head_portrait);
        navUserNameTextView = navHeaderView.findViewById(R.id.nav_user_name);
        navUserSignatureTextView = navHeaderView.findViewById(R.id.nav_personal_signature);

        initNavigationViewListener();
    }

    private void initBottomNavigationBar() {
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.measure_success, "已测量"))
                .addItem(new BottomNavigationItem(R.mipmap.measure, "测量"))
                .addItem(new BottomNavigationItem(R.mipmap.measure_draft, "草稿"))
                .initialise();
    }
    
    private void initNiceSpinner() {
        initPublishTimeSortNiceSpinner();

        initVarietyFilterNiceSpinner();
    }

    private void initPublishTimeSortNiceSpinner() {
        List<String> menuList = Arrays.asList("默认排序", "时间正序", "时间倒序");

        publishTimeSortNiceSpinner.attachDataSource(menuList);

        publishTimeSortNiceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    private void initVarietyFilterNiceSpinner() {
        List<String> menuList = Arrays.asList(AnimalConstant.AnimalVarietyEnum.Duroc_PIF.getVariety(), AnimalConstant.AnimalVarietyEnum.Landrace_PIG.getVariety(), AnimalConstant.AnimalVarietyEnum.Large_White_PIG.getVariety());

        varietyFilterNiceSpinner.attachDataSource(menuList);

        varietyFilterNiceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    /**
     * 渲染侧拉边栏头部信息
     */
    private void renderNavHeaderView() {
        LogUtil.V("渲染侧拉边栏头部");
        MobileUser mobileUser = getApp().getMobileUser();

        if (mobileUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return;
        }

        navUserNameTextView.setText(mobileUser.getUserName());

        if (!StringUtil.isEmpty(mobileUser.getUserHeadPortrait())) {
            Glide.with(this).load(mobileUser.getUserHeadPortrait()).into(navHeadPortraitCircleImageView);
        }

        if (!StringUtil.isEmpty(mobileUser.getSignature())) {
            navUserSignatureTextView.setText(mobileUser.getSignature());
        }
    }

    // 给menu中的item设置点击事件
    private void initNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.nav_user:
                    intent = new Intent(MainActivity.this, UserInformationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_data:
                    break;
                case R.id.nav_account:
                    break;
                case R.id.nav_setting:
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
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
