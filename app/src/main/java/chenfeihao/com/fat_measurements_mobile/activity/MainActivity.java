package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
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

    private Spinner publishTimeSortSpinner;

    private Spinner varietyFilterSpinner;

    private SearchView searchView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView mainRecyclerView;

    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI(){
        navigationView = findViewById(R.id.nav_view);
        // publishTimeSortNiceSpinner = findViewById(R.id.publish_time_sort);
        // varietyFilterNiceSpinner = findViewById(R.id.variety_filter);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        bottomNavigationBar = findViewById(R.id.main_bottom_bar);
        searchView = findViewById(R.id.main_search_view);

        initNavigationView();
        initBottomNavigationBar();
        initSpinner();
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);

        BottomNavigationItem[] bottomNavigationItemArray = {new BottomNavigationItem(R.mipmap.measure_success, "已测量"),
                new BottomNavigationItem(R.mipmap.measure, "测量"),
                new BottomNavigationItem(R.mipmap.measure_draft, "草稿")};

        bottomNavigationBar.addItem(bottomNavigationItemArray[0])
                .addItem(bottomNavigationItemArray[1])
                .addItem(bottomNavigationItemArray[2])
                .setActiveColor(R.color.darkGray)
                .setInActiveColor(R.color.darkGray)
                .setFirstSelectedPosition(0) //设置默认选中位置
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
    
    private void initSpinner() {
        initPublishTimeSortSpinner();

        initVarietyFilterSpinner();
    }

    private void initPublishTimeSortSpinner() {
        publishTimeSortSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initVarietyFilterSpinner() {
        varietyFilterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
