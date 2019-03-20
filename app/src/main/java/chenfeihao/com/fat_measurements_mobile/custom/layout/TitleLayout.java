package chenfeihao.com.fat_measurements_mobile.custom.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;

/**
 * 自定义的标题栏
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/20
 */
public class TitleLayout extends LinearLayout {
    /**
     * UI
     */
    private TextView back;
    private TextView title;

    public TitleLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_title, this);

        initUI();
    }

    private void initUI() {
        back = findViewById(R.id.custom_title_back);
        title = findViewById(R.id.custom_title_title);

        back.setOnClickListener(v -> {
            /**
             * 要返回Activity栈中的上一个Activity只需要结束当前Activity
             */
            ((Activity)getContext()).finish();
        });
    }

    public void setTitle(String activityTitle) {
        title.setText(activityTitle);
    }

}
