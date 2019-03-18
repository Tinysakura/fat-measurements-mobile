package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.InteractionConstant;
import chenfeihao.com.fat_measurements_mobile.constant.UserInformationConstant;
import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.http.constant.ResponseCodeEnum;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.UserHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    /**
     * 控件
     */
    private EditText accountEditText;

    private EditText pwdEditText;

    private Button loginButton;

    private ImageView headPortraitImageView;

    private ImageView showPwdImageView;

    private TextView registerIntentTextView;

    /**
     * data
     */
    private String userName;
    private String userPwd;
    private Integer pwdShowStatus;

    /**
     * retrofit
     */
    UserHttpService userHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUserHttpService();
        initData();
        initUI();
    }

    private void initUserHttpService() {
        userHttpService = ((App)getApplicationContext()).getRetrofit().create(UserHttpService.class);
    }

    private void initData() {
        pwdShowStatus = InteractionConstant.PasswordRelatedEnum.PASSWORD_CIPHERTEXT.getCode();
    }

    private void initUI() {
        accountEditText = findViewById(R.id.account_input);
        pwdEditText = findViewById(R.id.pwd_input);
        headPortraitImageView = findViewById(R.id.img_head_portrait);
        showPwdImageView = findViewById(R.id.img_show_pwd);
        registerIntentTextView = findViewById(R.id.register_intent);
        loginButton = findViewById(R.id.login_button);

        initLoginButtonActionListener();
        initAccountEditTextActionListener();
        initPwdImageViewActionListener();
        initRegisterIntentTextViewActionListener();
    }

    /**
     * 为提示去注册的TextView绑定点击事件
     */
    private void initRegisterIntentTextViewActionListener() {
        registerIntentTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 为切换密码是否明文显示的ImageView添加点击事件
     */
    private void initPwdImageViewActionListener() {
        showPwdImageView.setOnClickListener(v -> {
            if (InteractionConstant.PasswordRelatedEnum.PASSWORD_CIPHERTEXT.getCode().equals(pwdShowStatus)) {
                //设置EditText文本为可见的
                pwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                pwdShowStatus = InteractionConstant.PasswordRelatedEnum.PASSWORD_PLAINTEXT.getCode();
                Glide.with(this).load(R.mipmap.open_eyes).into(showPwdImageView);
            } else {
                //设置EditText文本为隐藏的
                pwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                pwdShowStatus = InteractionConstant.PasswordRelatedEnum.PASSWORD_CIPHERTEXT.getCode();
                Glide.with(this).load(R.mipmap.close_eyes).into(showPwdImageView);
            }

            pwdEditText.postInvalidate();
            //切换后将EditText光标置于末尾
            CharSequence charSequence = pwdEditText.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }

        });
    }

    /**
     * 为用户名输入框绑定监听事件
     */
    private void initAccountEditTextActionListener() {
        accountEditText.setOnFocusChangeListener((v, hasFocus) -> {
            /**
             * 当用户名不为空且当用户名输入框失焦时尝试去获取用户头像
             */
            if (!accountEditText.hasFocus() && !StringUtil.isEmpty(accountEditText.getText().toString())) {
                try {
                    userHttpService.getUserHeadPortrait(accountEditText.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(responseView -> {
                        Glide.with(this).load((String) responseView.getResult()).placeholder(R.mipmap.default_head_portrait).error(R.mipmap.default_head_portrait).into(headPortraitImageView);
                    });
                } catch (Exception e) {
                    LogUtil.V("获取用户头像失败");
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 为注册按钮绑定点击事件
     */
    private void initLoginButtonActionListener() {
        loginButton.setOnClickListener(v -> {
            userName = accountEditText.getText().toString();
            userPwd = pwdEditText.getText().toString();
            LogUtil.V("向服务器发送用户登录请求，userName:" + userName + " " + "userPwd:" + userPwd);

            Observable<ResponseView<ResponseView<UserDto>>> loginObservable = null;
            try {
                loginObservable = userHttpService.login(userName, userPwd);

                loginObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(responseView -> {
                    ResponseView result = (ResponseView)responseView.getResult();

                    /**
                     * 通过验证的情况
                     */
                    if (ResponseCodeEnum.OK.getCode().equals(result.getCode())) {
                        LogUtil.V("通过验证");
                        /**
                         * 将用户id等相关信息持久化存储
                         */
                        SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
                        MobileUser mobileUser = new MobileUser((UserDto) result.getResult(), System.currentTimeMillis() + UserInformationConstant.USER_INFORMATION_EXPIRE_TIME);
                        String userJson = JSON.toJSONString(mobileUser);
                        editor.putString("mobile_user", userJson);

                        /**
                         * 重新初始化Retrofit客户端
                         */
                        ((App)getApplicationContext()).initRetrofitWithUserInfo(mobileUser);

                        /**
                         * 跳转到MainActivity
                         */
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    /**
                     * 用户名错误的情况
                     */
                    if (ResponseCodeEnum.NO_USER.getCode().equals(result.getCode())) {
                        LogUtil.V("用户不存在");
                        Toast showToast = Toast.makeText(LoginActivity.this, "无此用户，请检查输入的用户名", Toast.LENGTH_SHORT);
                        showToast.setGravity(Gravity.CENTER, 0, 0);
                        showToast.show();
                    }

                    if (ResponseCodeEnum.PWD_ERROR.getCode().equals(result.getCode())) {
                        LogUtil.V("密码错误");
                        Toast showToast = Toast.makeText(LoginActivity.this, "密码错误，请重新输入密码", Toast.LENGTH_SHORT);
                        showToast.setGravity(Gravity.CENTER, 0, 0);
                        showToast.show();

                        pwdEditText.getText().clear();
                    }
                });

            } catch (Exception e) {
                LogUtil.V("登录失败");
                e.printStackTrace();
            }
        });
    }
}
