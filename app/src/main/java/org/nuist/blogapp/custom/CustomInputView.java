package org.nuist.blogapp.custom;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.nuist.blogapp.R;

/**
 * 自定义的登录输入框
 */
public class CustomInputView extends LinearLayout {
    private EditText editInput;
    private TextView textAction;

    private CountDownTimer countDownTimer;
    private boolean isPasswordToggle = false;

    public CustomInputView(Context context) {
        super(context);
        init(context);
    }

    public CustomInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_custom_input, this, true);
        editInput = findViewById(R.id.edit_input);
        textAction = findViewById(R.id.text_action);
    }

    public void setHint(String hint) {
        editInput.setHint(hint);
    }

    public String getText() {
        return editInput.getText().toString().trim();
    }

    public void setActionText(String actionText, OnClickListener listener) {
        textAction.setText(actionText);
        textAction.setVisibility(View.VISIBLE);
        textAction.setOnClickListener(listener);
    }

    public void hideAction() {
        textAction.setVisibility(View.GONE);
    }

    public EditText getEditText() {
        return editInput;
    }

    // ============ 密码可见切换 ============
    public void enablePasswordToggle() {
        isPasswordToggle = true;
        textAction.setText("显示");
        textAction.setVisibility(View.VISIBLE);

        textAction.setOnClickListener(v -> {
            if (editInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                textAction.setText("隐藏");
            } else {
                editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                textAction.setText("显示");
            }
            editInput.setSelection(editInput.getText().length());
        });
        editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    // ============ 启动验证码倒计时 ============
    public void startCountdown(int seconds) {
        textAction.setEnabled(false);
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                textAction.setText((millisUntilFinished / 1000) + "s");
            }

            public void onFinish() {
                textAction.setText("重新获取");
                textAction.setEnabled(true);
            }
        }.start();
    }

    public void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        textAction.setText("获取验证码");
        textAction.setEnabled(true);
    }
}
