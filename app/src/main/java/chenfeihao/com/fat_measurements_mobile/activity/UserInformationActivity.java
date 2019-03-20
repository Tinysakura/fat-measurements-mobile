package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.custom.layout.SelectPicPopWindow;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.UserHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import chenfeihao.com.fat_measurements_mobile.util.DensityUtil;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;

public class UserInformationActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout customTitle;

    private EditText alterUserNameEditText;

    private EditText alterUserSignatureEditText;

    private EditText alterUserPwdEditText;

    private TextView alterUserHeadPortraitTextView;

    /**
     * retrofit
     */
    UserHttpService userHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initUI();
        initRetrofit();
    }

    private void initRetrofit() {
        userHttpService = ((App)getApplicationContext()).getRetrofit().create(UserHttpService.class);
    }

    private void initUI() {
        customTitle = findViewById(R.id.user_custom_title);
        customTitle.setTitle("编辑资料");

        alterUserNameEditText = findViewById(R.id.alter_user_name);
        alterUserSignatureEditText = findViewById(R.id.alter_user_signature);
        alterUserPwdEditText = findViewById(R.id.alter_user_pwd);
        alterUserHeadPortraitTextView = findViewById(R.id.alter_user_head_portrait);

        MobileUser mobileUser = getApp().getMobileUser();
        alterUserNameEditText.setText(mobileUser.getUserName());
        alterUserPwdEditText.setText(mobileUser.getUserPassword());
        alterUserSignatureEditText.setText(mobileUser.getSignature());

        alterUserHeadPortraitTextView.setOnClickListener(v -> {
            PopupWindow selectPicPopWindow = new SelectPicPopWindow(UserInformationActivity.this, null);
            selectPicPopWindow.showAtLocation(findViewById(R.id.activity_user_information), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, DensityUtil.getBottomBarHeight(this) + 15);
        });
    }

    /**
     * 在onPause生命周期中将修改同步到服务端
     */
    @Override
    protected void onPause() {
        super.onPause();

        MobileUser mobileUser = getApp().getMobileUser();
        UserDto userDto = new UserDto();
        userDto.setId(mobileUser.getId());
        userDto.setUserName(alterUserNameEditText.getText().toString());
        userDto.setUserPassword(alterUserPwdEditText.getText().toString());

        try {
            userHttpService.updateUserInfo(userDto);
            LogUtil.V("用户信息更新成功");
        } catch (Exception e) {
            LogUtil.V("用户信息更新失败");
            e.printStackTrace();
        }
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
