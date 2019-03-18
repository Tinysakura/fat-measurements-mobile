package chenfeihao.com.fat_measurements_mobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.constant.enums.RegisterStepEnum;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initStep();
        initUI();
    }

    private void initUI() {
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view);
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

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editText.getText().toString();

                if (!StringUtil.isEmpty(userName)) {
                    renderStep2UI();
                } else {
                    textView.setText(R.string.user_name_blank_prompt);
                }
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

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = editText.getText().toString();

                if (!StringUtil.isEmpty(userPwd)) {
                    renderStep3UI();
                } else {
                    textView.setText(R.string.user_pwd_blank_prompt);
                }
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

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondPwd = editText.getText().toString();

                if (userPwd.equals(secondPwd)) {
                    renderStep4UI();
                } else {
                    textView.setText(R.string.pwd_different_prompt);
                    editText.getText().clear();
                }
            }
        });
    }

    /**
     * 渲染注册第四步，确认注册的界面UI
     */
    private void renderStep4UI() {
        button.setText(R.string.register);
        textView.setText(R.string.pwd_affirm_prompt);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
