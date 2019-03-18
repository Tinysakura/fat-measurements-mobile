package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.constant.enums.RegisterStepEnum;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.UserHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    /**
     *控件
     */
    private EditText editText;

    private Button button;

    private TextView textView;

    /**
     * 数据
     */
    private Integer step;
    private String userName;
    private String userPwd;
    private String secondPwd;

    /**
     * retrofit
     */
    private UserHttpService userHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initStep();
        initUI();
        initUserHttpService();
    }

    private void initUserHttpService() {
        userHttpService = ((App)getApplicationContext()).getRetrofit().create(UserHttpService.class);
    }

    private void initUI() {
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view);

        renderStep1UI();
    }

    private void initStep() {
        this.step = RegisterStepEnum.ENTER_USER_NAME.getCode();
    }

    /**
     * 渲染注册第一步，用户输入用户名的界面UI
     */
    private void renderStep1UI() {
        editText.setHint(R.string.user_name_placeholder);
        button.setText(R.string.next_step);
        textView.setText(R.string.user_name_prompt);

        button.setOnClickListener(v -> {
            userName = editText.getText().toString();

            if (!StringUtil.isEmpty(userName)) {
                renderStep2UI();
                editText.getText().clear();
            } else {
                textView.setText(R.string.user_name_blank_prompt);
            }
        });
    }

    /**
     * 渲染注册第二步，用户输入密码的界面UI
     */
    private void renderStep2UI() {
        editText.setHint(R.string.user_pwd_placeholder);
        button.setText(R.string.next_step);
        textView.setText(R.string.user_pwd_prompt);

        button.setOnClickListener(v -> {
            userPwd = editText.getText().toString();

            if (!StringUtil.isEmpty(userPwd)) {
                renderStep3UI();
                editText.getText().clear();
            } else {
                textView.setText(R.string.user_pwd_blank_prompt);
            }
        });
    }

    /**
     * 渲染注册第三步，用户确认密码的界面UI
     */
    private void renderStep3UI() {
        editText.setHint(R.string.pwd_affirm_placeholder);
        button.setText(R.string.next_step);
        textView.setText(R.string.user_pwd_prompt);

        button.setOnClickListener(v -> {
            secondPwd = editText.getText().toString();

            if (userPwd.equals(secondPwd)) {
                renderStep4UI();
            } else {
                textView.setText(R.string.pwd_different_prompt);
                editText.getText().clear();
            }
        });
    }

    /**
     * 渲染注册第四步，确认注册的界面UI
     */
    private void renderStep4UI() {
        button.setText(R.string.register);
        textView.setText(R.string.pwd_affirm_prompt);
        editText.setVisibility(View.INVISIBLE);

        button.setOnClickListener(v -> {
            /**
             * 向服务端发送注册的网络请求
             */
            LogUtil.V("向服务器发送用户注册请求，userName:" + userName + " " + "userPwd:" + userPwd);
            UserDto userDto = new UserDto();
            userDto.setUserName(userName);
            userDto.setUserPassword(userPwd);
            try {
                userHttpService.rigister(userDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(userDtoResponseView -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                });
            } catch (Exception e) {
                LogUtil.V("注册失败");
                e.printStackTrace();
            }

        });
    }
}
