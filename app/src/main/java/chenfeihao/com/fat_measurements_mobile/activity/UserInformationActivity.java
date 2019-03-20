package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.custom.layout.SelectPicPopWindow;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.UserHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import chenfeihao.com.fat_measurements_mobile.util.DensityUtil;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserInformationActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout customTitle;

    private EditText alterUserNameEditText;

    private EditText alterUserSignatureEditText;

    private EditText alterUserPwdEditText;

    private TextView alterUserHeadPortraitTextView;

    private TextView saveEditUserInfoTextView;

    /**
     * retrofit
     */
    UserHttpService userHttpService;

    /**
     * data
     */
    private String headPortraitUrl;

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
        saveEditUserInfoTextView = findViewById(R.id.save_edit_user_info);

        MobileUser mobileUser = getApp().getMobileUser();
        alterUserNameEditText.setText(mobileUser.getUserName());
        alterUserPwdEditText.setText(mobileUser.getUserPassword());
        alterUserSignatureEditText.setText(mobileUser.getSignature());

        initAlterUserHeadPortraitTextViewListener();
        initSaveEditUserInfoTextViewListener();
        // initAlterUserSignatureEditTextListener();
    }

    private void initSaveEditUserInfoTextViewListener() {
        saveEditUserInfoTextView.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("提示");
            dialog.setMessage("确定要保存编辑的内容吗?");
            dialog.setCancelable(false);

            dialog.setPositiveButton("确定", (dialog1, which) -> {
                saveEditUserInfo();
                dialog1.dismiss();
                finish();
            });
            dialog.setNegativeButton("放弃", (dialog2, which) -> dialog2.dismiss());

            dialog.show();
        });
    }

    private void initAlterUserHeadPortraitTextViewListener() {
        alterUserHeadPortraitTextView.setOnClickListener(v -> {
            PopupWindow selectPicPopWindow = new SelectPicPopWindow(UserInformationActivity.this, null);
            selectPicPopWindow.showAtLocation(findViewById(R.id.activity_user_information), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, DensityUtil.getBottomBarHeight(this) + 15);
        });
    }

    private void initAlterUserSignatureEditTextListener() {
        alterUserSignatureEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!alterUserSignatureEditText.hasFocus()) {
                    SharedPreferences.Editor sharedPreferences = getSharedPreferences("user_signature", MODE_PRIVATE).edit();
                    sharedPreferences.putString("user_signature", alterUserSignatureEditText.getText().toString());
                    sharedPreferences.apply();
                }
            }
        });
    }

    private void updatePersonalSignature() {
        SharedPreferences.Editor sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE).edit();
        sharedPreferences.putString("user_signature", alterUserSignatureEditText.getText().toString());
        sharedPreferences.apply();
    }

    private void saveEditUserInfo() {
        updatePersonalSignature();

        MobileUser mobileUser = getApp().getMobileUser();
        UserDto userDto = new UserDto();
        userDto.setId(mobileUser.getId());
        userDto.setUserName(alterUserNameEditText.getText().toString());
        userDto.setUserPassword(alterUserPwdEditText.getText().toString());

        try {
            userHttpService.updateUserInfo(userDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(stringResponseView -> LogUtil.V("用户信息更新成功"));

            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            String mobileUserJsonStr = sharedPreferences.getString("mobile_user", null);

            MobileUser oldUserInfo = JSON.parseObject(mobileUserJsonStr, MobileUser.class);
            oldUserInfo.setUserName(alterUserNameEditText.getText().toString());
            oldUserInfo.setUserPassword(alterUserPwdEditText.getText().toString());
            oldUserInfo.setUserHeadPortrait(headPortraitUrl);

            SharedPreferences.Editor userDataEditor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
            userDataEditor.putString("mobile_user", JSON.toJSONString(oldUserInfo));
            userDataEditor.apply();

            getApp().initUserInfo();
        } catch (Exception e) {
            LogUtil.V("用户信息更新失败");
            e.printStackTrace();
        }
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
