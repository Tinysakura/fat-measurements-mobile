package chenfeihao.com.fat_measurements_mobile.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.SearchView;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.custom.adapter.AnimalDataAdapter;
import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.AnimalDataHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import chenfeihao.com.fat_measurements_mobile.util.UriPathSwitchUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    /**
     * data
     */
    private static final int SELECT_LOCAL_FILE = 3;

    // 已完成测量的数据
    private List<AnimalDataDto> animalDataDtoList;

    // 处于草稿状态的数据
    private List<AnimalDataDto> animalDataDtoDraftList;

    private AnimalDataAdapter animalDataAdapter;

    /**
     * retrofit
     */
    private AnimalDataHttpService animalDataHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initRetrofitClient();
    }

    private void initRetrofitClient() {
        animalDataHttpService = getApp().getRetrofit().create(AnimalDataHttpService.class);
    }

    private void initUI(){
        navigationView = findViewById(R.id.nav_view);
        publishTimeSortSpinner = findViewById(R.id.publish_time_sort);
        varietyFilterSpinner = findViewById(R.id.variety_filter);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        bottomNavigationBar = findViewById(R.id.main_bottom_bar);
        searchView = findViewById(R.id.main_search_view);

        initNavigationView();
        initBottomNavigationBar();
        initSpinner();
        initSearchView();
        initRecycleView();
    }

    private void initRecycleView() {
        // 两列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mainRecyclerView.setLayoutManager(gridLayoutManager);

        initUserAnimalData();

        animalDataAdapter = new AnimalDataAdapter(animalDataDtoList);
        mainRecyclerView.setAdapter(animalDataAdapter);
    }

    /**
     * 使用新的数据重新渲染recycleView
     * @param data
     */
    private void reRenderRecycleView(List<AnimalDataDto> data) {
        animalDataAdapter = new AnimalDataAdapter(data);
        mainRecyclerView.setAdapter(animalDataAdapter);
    }

    private void initUserAnimalData() {
        try {
            animalDataHttpService.getUserAnimalData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(animalDataDtoResponseView -> {
                LogUtil.V("获取用户动物数据成功");

                List<AnimalDataDto> requestResult = animalDataDtoResponseView.getResult();
                /**
                 * 开始分离完成状态与草稿状态的数据
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    animalDataDtoList = requestResult.stream().filter(e -> AnimalConstant.AnimalDraftEnum.NOT_DRAFT.getCode().equals(e.getAnimalDraft())).collect(Collectors.toList());
                } else {
                    for (AnimalDataDto animalDataDto : requestResult) {
                        if (AnimalConstant.AnimalDraftEnum.NOT_DRAFT.getCode().equals(animalDataDto.getAnimalDraft())) {
                            animalDataDtoList.add(animalDataDto);
                        }
                    }
                }

                requestResult.removeAll(animalDataDtoList);
                animalDataDtoDraftList = requestResult;
            });
        } catch (Exception e) {
            LogUtil.V("获取用户动物数据失败");
            e.printStackTrace();
        }
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
        bottomNavigationBar.clearAll();
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
                switch (position) {
                    case 0:
                        /**
                         * 查看已测量完成的数据
                         */
                        reRenderRecycleView(animalDataDtoList);
                        break;
                    case 1:
                        /**
                         * 测量新的数据
                         * 打开本地文件管理器选择B超文件
                         */
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");//无类型限制
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, SELECT_LOCAL_FILE);
//                        Intent intent = new Intent(MainActivity.this, MeasureActivity.class);
//                        startActivity(intent);
                        break;
                    case 2:
                        /**
                         * 查看草稿状态的数据
                         */
                        reRenderRecycleView(animalDataDtoDraftList);
                        break;
                }
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
        publishTimeSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initVarietyFilterSpinner() {
        varietyFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        initBottomNavigationBar();
    }

    private void jump2Login() {
        if (getApp().getMobileUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_FILE:
                    Uri uri = data.getData();
                    String realPath = UriPathSwitchUtil.getPathByUri4kitkat(this, uri);

                    Intent intent = new Intent(MainActivity.this, MeasureActivity.class);
                    intent.putExtra("file_path", realPath);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
