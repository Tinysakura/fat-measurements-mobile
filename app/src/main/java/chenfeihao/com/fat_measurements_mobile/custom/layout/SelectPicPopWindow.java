package chenfeihao.com.fat_measurements_mobile.custom.layout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Method;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.custom.listener.SelectPicListener;

/**
 * 图片选择PopWindow
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/20
 */
public class SelectPicPopWindow extends PopupWindow {
    /**
     * UI
     */
    private TextView picFromCameraTextView;

    private TextView picFromStorageTextView;

    private TextView picFromCancelTextView;

    /**
     * Listener
     */
    private SelectPicListener selectPicListener;

    public SelectPicPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);

        initUI(context);
    }

    public void setSelectPicListener(SelectPicListener selectPicListener) {
        this.selectPicListener = selectPicListener;
    }

    private void initUI(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View popWindow = inflater.inflate(R.layout.layout_popwindow_select_pic, null);

        picFromCameraTextView = popWindow.findViewById(R.id.pic_from_camera);
        picFromStorageTextView = popWindow.findViewById(R.id.pic_from_storage);
        picFromCancelTextView = popWindow.findViewById(R.id.pic_from_cancel);

        //设置SelectPicPopupWindow的View
        this.setContentView(popWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.animTranslate);

        if (hasNavBar(context)) {
            // hideBottomUIMenu(context);
        }

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popWindow.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = popWindow.findViewById(R.id.pic_from_camera).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                        // showBottomUIMenu(context);
                    }
                }
                return true;
            }
        });

        initPicFromCancelTextViewListener();

        initSelectPicFromAnyListener();
    }

    private void initPicFromCancelTextViewListener() {
        picFromCancelTextView.setOnClickListener(view -> {
            /**
             * 取消选择按钮将销毁弹出框
             */
            dismiss();
            // showBottomUIMenu(context);
        });
    }

    private void initSelectPicFromAnyListener() {
        picFromCameraTextView.setOnClickListener(v -> {
            selectPicListener.selectPicFromCameraListener();
        });

        picFromStorageTextView.setOnClickListener(v -> {
            selectPicListener.selectPicFromStorageListener();
        });
    }

    protected void hideBottomUIMenu(Context context){
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = ((Activity)context).getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = ((Activity)context).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 检查是否存在虚拟按键栏
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 如果底部的bar 隐藏就显示
     */
    protected void showBottomUIMenu(Context context) {
        int flags;
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        // This work only for android 4.4+
        if(curApiVersion >= Build.VERSION_CODES.KITKAT){
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        }else{
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }

        // must be executed in main thread :)
        ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
