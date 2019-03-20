package chenfeihao.com.fat_measurements_mobile.custom.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;

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

    private TextView picFromCancleTextView;

    public SelectPicPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);

        initUI(context);
    }

    private void initUI(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View popWindow = inflater.inflate(R.layout.layout_popwindow_select_pic, null);

        picFromCameraTextView = popWindow.findViewById(R.id.pic_from_camera);
        picFromStorageTextView = popWindow.findViewById(R.id.pic_from_storage);
        picFromCancleTextView = popWindow.findViewById(R.id.pic_from_cancel);

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
        hideBottomUIMenu(context);


        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popWindow.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = popWindow.findViewById(R.id.pic_from_camera).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                        showBottomUIMenu(context);
                    }
                }
                return true;
            }
        });


        picFromCancleTextView.setOnClickListener(view -> {
            /**
             * 取消选择按钮将销毁弹出框
             */
            dismiss();
            showBottomUIMenu(context);
        });

    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu(Context context) {
        int flags;
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        // This work only for android 4.4+
        if(curApiVersion >= Build.VERSION_CODES.KITKAT){
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }else{
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }

        // must be executed in main thread :)
        ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(flags);
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
