package chenfeihao.com.fat_measurements_mobile.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.animTranslate);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popWindow.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = popWindow.findViewById(R.id.pic_from_camera).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
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
        });

    }
}
