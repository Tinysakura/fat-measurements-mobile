package chenfeihao.com.fat_measurements_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.custom.adapter.AnimalDataAdapter;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.AnimalDataHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import chenfeihao.com.fat_measurements_mobile.util.UriPathSwitchUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    /**
     * UI
     */
    private DrawerLayout mainDrawerLayout;

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

    private ImageView emptySearchResultImageView;

    BottomNavigationItem[] bottomNavigationItemArray;

    /**
     * data
     */
    private static final int SELECT_LOCAL_FILE = 3;

    private Integer bottomBarSelectedPosition = 0;

    // 已完成测量的数据
    private List<AnimalDataDto> animalDataDtoList = new ArrayList<>(16);

    // 处于草稿状态的数据
    private List<AnimalDataDto> animalDataDtoDraftList = new ArrayList<>(16);

    // 已完成测量的数据中符合用户过滤条件的部分
    private List<AnimalDataDto> animalDataDtoFilterList;

    // 处于草稿状态的数据中符合用户过滤条件的部分
    private List<AnimalDataDto> animalDataDtoDraftFilterList;

    private AnimalDataAdapter animalDataAdapter;

    private static final Integer[] animalVarietyArray = new Integer[]{101, 102, 103};

    /**
     * retrofit
     */
    private AnimalDataHttpService animalDataHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofitClient();
        initUI();
    }

    private void initRetrofitClient() {
        animalDataHttpService = getApp().getRetrofit().create(AnimalDataHttpService.class);
    }

    private void initUI(){
        mainDrawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        publishTimeSortSpinner = findViewById(R.id.publish_time_sort);
        varietyFilterSpinner = findViewById(R.id.variety_filter);
        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh);
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        bottomNavigationBar = findViewById(R.id.main_bottom_bar);
        searchView = findViewById(R.id.main_search_view);
        emptySearchResultImageView = findViewById(R.id.main_empty_search_result);

        initBottomNavigationItem();
        initRecycleView();
        initNavigationView();
        initBottomNavigationBar();
        initSearchView();
        initSwipeRefreshLayout();
        initSpinner();
    }

    private void initBottomNavigationItem() {
        bottomNavigationItemArray = new BottomNavigationItem[]{new BottomNavigationItem(R.mipmap.measure_success, "已测量"),
                new BottomNavigationItem(R.mipmap.measure, "测量"),
                new BottomNavigationItem(R.mipmap.measure_draft, "草稿")};
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            initUserAnimalData(countDownLatch);

            countDownLatch.countDown();
            animalDataAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initRecycleView() {
        // 两列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mainRecyclerView.setLayoutManager(gridLayoutManager);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            initUserAnimalData(countDownLatch);

            countDownLatch.await();

            animalDataAdapter = new AnimalDataAdapter(animalDataDtoFilterList);
            mainRecyclerView.setAdapter(animalDataAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用新的数据重新渲染recycleView
     * 更新角标
     * @param data
     */
    private void reRenderRecycleView(List<AnimalDataDto> data) {
        animalDataAdapter = new AnimalDataAdapter(data);
        mainRecyclerView.setAdapter(animalDataAdapter);

        showBadgeView(0, animalDataDtoFilterList.size());
        showBadgeView(2, animalDataDtoDraftFilterList.size());
    }

    private void initUserAnimalData(CountDownLatch countDownLatch) {
        try {
            animalDataHttpService.getUserAnimalData().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(animalDataDtoResponseView -> {
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
                LogUtil.V(JSON.toJSONString(animalDataDtoList));

                requestResult.removeAll(animalDataDtoList);
                animalDataDtoDraftList = requestResult;
                LogUtil.V(JSON.toJSONString(animalDataDtoDraftList));

                /**
                 * 根据用户的限制条件过滤出符合符合条件的数据
                 */
                Integer publishTimeSelectItem = publishTimeSortSpinner.getSelectedItemPosition();
                Integer varietySelectItem = varietyFilterSpinner.getSelectedItemPosition();

                animalDataDtoFilterList = dataFilter(animalDataDtoList, publishTimeSelectItem, varietySelectItem);
                LogUtil.V(JSON.toJSONString(animalDataDtoFilterList));
                animalDataDtoDraftFilterList = dataFilter(animalDataDtoDraftList, publishTimeSelectItem, varietySelectItem);
                LogUtil.V(JSON.toJSONString(animalDataDtoDraftFilterList));

                countDownLatch.countDown();
            });
        } catch (Exception e) {
            LogUtil.V("获取用户动物数据失败");
            e.printStackTrace();
        }
    }

    /**
     * 过滤出符合条件的数据
     *
     * @param animalDataDtoList 原始数据
     * @param timeCondition     时间条件
     * @param varietyCondition  品种条件
     * @return
     */
    private List<AnimalDataDto> dataFilter(List<AnimalDataDto> animalDataDtoList, Integer timeCondition, Integer varietyCondition) {
        List<AnimalDataDto> resultDtoList = animalDataDtoList;

        /**
         * 过滤品种
         * varietyCondition为0时表示为全部品种，此时不需要过滤
         */
        if (!varietyCondition.equals(0)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                resultDtoList = animalDataDtoList.stream().filter(e -> animalVarietyArray[varietyCondition - 1].equals(e.getAnimalVariety())).collect(Collectors.toList());
            } else {
                List<AnimalDataDto> shouldFilter = new ArrayList<>();

                for (AnimalDataDto animalDataDto : animalDataDtoList) {
                    if (!animalVarietyArray[varietyCondition - 1].equals(animalDataDto.getAnimalVariety())) {
                        shouldFilter.add(animalDataDto);
                    }
                }

                resultDtoList = new ArrayList<>(animalDataDtoList);
                resultDtoList.removeAll(shouldFilter);
            }
        }

        sortedByTime(resultDtoList, timeCondition);

        return resultDtoList;
    }

    /**
     * 按时间排序
     * 0 不排序， 1 时间升序 2 时间倒序
     */
    private void sortedByTime(List<AnimalDataDto> resultDtoList, Integer timeCondition) {
        switch (timeCondition) {
            case 0:
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    resultDtoList = resultDtoList.stream().sorted((e1, e2) -> {
                        return e1.getDbUpdateTime().compareTo(e2.getDbCreateTime());
                    }).collect(Collectors.toList());
                } else {
                    Collections.sort(resultDtoList, (o1, o2) -> o1.getDbUpdateTime().compareTo(o2.getDbUpdateTime()));
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    resultDtoList = resultDtoList.stream().sorted((e1, e2) -> {
                        return e2.getDbUpdateTime().compareTo(e1.getDbCreateTime());
                    }).collect(Collectors.toList());
                } else {
                    Collections.sort(resultDtoList, (o1, o2) -> o2.getDbUpdateTime().compareTo(o1.getDbUpdateTime()));
                }
                break;
            default:
                break;
        }
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AnimalDataDto result = findAnimalDataDtoById(query);

                if (result == null) {
                    emptySearchResultImageView.setVisibility(View.VISIBLE);
                    mainRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    switch (bottomBarSelectedPosition) {
                        case 0:
                            animalDataDtoFilterList = Arrays.asList(result);
                            reRenderRecycleView(animalDataDtoFilterList);
                            break;
                        case 2:
                            animalDataDtoDraftFilterList = Arrays.asList(result);
                            reRenderRecycleView(animalDataDtoDraftFilterList);
                            break;
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (StringUtil.isEmpty(newText)) {
                    emptySearchResultImageView.setVisibility(View.INVISIBLE);
                    mainRecyclerView.setVisibility(View.VISIBLE);

                    return true;
                }

                AnimalDataDto result = findAnimalDataDtoById(newText);

                if (result == null) {
                    emptySearchResultImageView.setVisibility(View.VISIBLE);
                    mainRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    switch (bottomBarSelectedPosition) {
                        case 0:
                            animalDataDtoFilterList = Arrays.asList(result);
                            reRenderRecycleView(animalDataDtoFilterList);
                            break;
                        case 2:
                            animalDataDtoDraftFilterList = Arrays.asList(result);
                            reRenderRecycleView(animalDataDtoDraftFilterList);
                            break;
                    }
                }

                return true;
            }
        });

//        searchView.setOnFocusChangeListener((v, hasFocus) -> {
//
//        });
    }

    /**
     * 根据动物id查询指定的测量信息
     * @param animalId
     * @return
     */
    private AnimalDataDto findAnimalDataDtoById(String animalId) {
        switch (bottomBarSelectedPosition) {
            case 0:
                for (AnimalDataDto animalDataDto : animalDataDtoFilterList) {
                    if (animalDataDto.getAnimalId().equals(animalId)) {
                        return animalDataDto;
                    }
                }
                break;
            case 2:
                for (AnimalDataDto animalDataDto : animalDataDtoDraftFilterList) {
                    if (animalDataDto.getAnimalId().equals(animalId)) {
                        return animalDataDto;
                    }
                }
                break;
        }

        return null;
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

        bottomNavigationBar.addItem(bottomNavigationItemArray[0])
                .addItem(bottomNavigationItemArray[1])
                .addItem(bottomNavigationItemArray[2])
                .setActiveColor(R.color.colorPrimaryDark)
                .setInActiveColor(R.color.darkGray)
                .setFirstSelectedPosition(0) //设置默认选中位置
                .initialise();

        bottomBarSelectedPosition = 0;

        showBadgeView(0, animalDataDtoFilterList.size());
        showBadgeView(2, animalDataDtoDraftFilterList.size());

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        /**
                         * 查看已测量完成的数据
                         */
                        reRenderRecycleView(animalDataDtoFilterList);
                        LogUtil.V("触发回调");
                        bottomBarSelectedPosition = position;
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
                        bottomBarSelectedPosition = position;
//                        Intent intent = new Intent(MainActivity.this, MeasureActivity.class);
//                        startActivity(intent);
                        break;
                    case 2:
                        /**
                         * 查看草稿状态的数据
                         */
                        reRenderRecycleView(animalDataDtoDraftFilterList);
                        bottomBarSelectedPosition = position;
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
                LogUtil.V("publishTime spinner select:" + position);
                if (bottomBarSelectedPosition.equals(0)) {
                    sortedByTime(animalDataDtoFilterList, position);
                    reRenderRecycleView(animalDataDtoFilterList);
                } else if (bottomBarSelectedPosition.equals(2)) {
                    sortedByTime(animalDataDtoDraftFilterList, position);
                    reRenderRecycleView(animalDataDtoDraftFilterList);
                }
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
                LogUtil.V("variety spinner select:" + position);
                Integer publishTimeSelectItem = publishTimeSortSpinner.getSelectedItemPosition();

                if (bottomBarSelectedPosition.equals(0)) {
                    animalDataDtoFilterList = dataFilter(animalDataDtoList, publishTimeSelectItem, position);
                    reRenderRecycleView(animalDataDtoFilterList);
                } else if (bottomBarSelectedPosition.equals(2)) {
                    animalDataDtoDraftFilterList = dataFilter(animalDataDtoDraftList, publishTimeSelectItem, position);
                    reRenderRecycleView(animalDataDtoDraftFilterList);
                }
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
                    /**
                     * 在MainActivity隐藏NavigationView
                     */
                    mainDrawerLayout.closeDrawers();
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

            finish();
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

    /**
     * 在bottomNavigationBar的指定tab上显示角标
     * @param viewIndex
     * @param showNumber
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        BadgeItem badge=new BadgeItem()
                .setBorderWidth(2)//Badge的Border(边界)宽度
//                .setBorderColor("#FF0000")//Badge的Border颜色
                .setBackgroundColor("#FF0000")//Badge背景颜色
                .setText(showNumber + "")//显示的文本
                .setTextColor("#FFFFFF")//文本颜色
//                .setAnimationDuration(2000)
                .setHideOnSelect(false);//当选中状态时消失，非选中状态显示

        bottomNavigationItemArray[viewIndex].setBadgeItem(badge);
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
