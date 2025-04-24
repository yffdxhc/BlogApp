package org.nuist.blogapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.custom.CustomInputView;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    // ViewModel
    private UserViewModel userViewModel;

    // 邮箱布局
    private ViewStub loginStubEmail;
    // 账密布局
    private ViewStub loginStubAccount;
    // 单选框
    private RadioGroup loginChoice;
    // 返回布局
    private ImageView loginChoiceBack;
    // 保存布局的引用
    private View emailLayout; // 保存邮箱布局的引用
    private View accountLayout; // 保存账号布局的引用

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(LoginActivity.this).get(UserViewModel.class);
        loginStubEmail = findViewById(R.id.login_stub_email);
        loginStubAccount = findViewById(R.id.login_stub_account);
        loginChoice = findViewById(R.id.login_choices);
        loginChoiceBack = findViewById(R.id.login_choice_back);

        setListener();
        showEmailLayout();
    }

    // 展示邮箱登录
    private void showEmailLayout() {
        if (emailLayout == null) {
            emailLayout = loginStubEmail.inflate();

            CustomInputView emailInput = emailLayout.findViewById(R.id.email_input);
            emailInput.setHint("请输入邮箱");

            CustomInputView codeInput = emailLayout.findViewById(R.id.code_input);
            codeInput.setHint("请输入验证码");

            // 验证邮箱格式后再发送验证码
            codeInput.setActionText("获取验证码", v -> {
                String email = emailInput.getText();
                if (!isValidEmail(email)) {
                    Toast.makeText(this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "验证码已发送到 " + email, Toast.LENGTH_SHORT).show();
                codeInput.startCountdown(60); // 倒计时60秒

                // TODO: 调用接口发送验证码
            });
        }
        emailLayout.setVisibility(View.VISIBLE); // 显示
        // 隐藏另一个布局
        if (accountLayout != null) {
            accountLayout.setVisibility(View.GONE);
        }
    }

    private void showAccountLayout() {
        if (accountLayout == null) {
            accountLayout = loginStubAccount.inflate();

            CustomInputView accountInput = accountLayout.findViewById(R.id.account_input);
            accountInput.setHint("请输入账号");

            CustomInputView passwordInput = accountLayout.findViewById(R.id.password_input);
            passwordInput.setHint("请输入密码");
            passwordInput.enablePasswordToggle(); // 开启密码可见切换

            passwordInput.setActionText("忘记密码", v -> {
                Toast.makeText(this, "跳转找回密码", Toast.LENGTH_SHORT).show();
            });
        }
        accountLayout.setVisibility(View.VISIBLE);
        if (emailLayout != null) {
            emailLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 事件绑定
     */
    private void setListener(){
        // 单选框切换登录方式
        loginChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的 RadioButton
                RadioButton radioButton  = group.findViewById(checkedId);
                // 展现内容
                Toast.makeText(LoginActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                // 根据单选按钮显示不同的登录布局
                if (checkedId == R.id.login_choice_email){
                    showEmailLayout();
                }else if (checkedId == R.id.login_choice_account){
                    showAccountLayout();
                }
            }
        });

        // 返回按钮
        loginChoiceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 登录按钮，在这里获取token
        loginStubAccount.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                CustomInputView accountInput = inflated.findViewById(R.id.account_input);
                CustomInputView passwordInput = inflated.findViewById(R.id.password_input);
                Button loginButton = inflated.findViewById(R.id.btn_account_login);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String account = accountInput.getText();
                        String password = passwordInput.getText();
                        if (account.isEmpty() || password.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d(TAG, "登录按钮loginStubAccount: 账号"+ account+ "密码"+ password);
                        userViewModel.setToken(new HashMap<String, RequestBody>(){{
                            put("user_id", RequestBody.create(MediaType.parse("text/plain"), account));
                            put("password", RequestBody.create(MediaType.parse("text/plain"), password));
                        }}).observe(LoginActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s != null && !s.isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}