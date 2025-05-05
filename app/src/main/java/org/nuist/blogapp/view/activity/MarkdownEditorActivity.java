package org.nuist.blogapp.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import org.nuist.blogapp.R;
import org.nuist.blogapp.util.MarkwonHelper;

import io.noties.markwon.Markwon;

public class MarkdownEditorActivity extends AppCompatActivity {

    private EditText editMarkdown;
    private TextView textPreview;
    private ScrollView scrollPreview;
    private boolean isPreviewMode = false;
    private Markwon markwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_editor);

        // 初始化视图
        editMarkdown = findViewById(R.id.edit_markdown);
        textPreview = findViewById(R.id.text_preview);
        scrollPreview = findViewById(R.id.scroll_preview);

        // 初始化 Markwon
        markwon = MarkwonHelper.getInstance(this);

        // 自动保存草稿
        editMarkdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSharedPreferences("markdown_draft", MODE_PRIVATE)
                        .edit().putString("draft_text", s.toString()).apply();
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // 恢复草稿
        String savedText = getSharedPreferences("markdown_draft", MODE_PRIVATE)
                .getString("draft_text", "");
        editMarkdown.setText(savedText);

        setupTopBar();
        setupMarkdownToolbar();
    }

    private void setupTopBar() {
        // 设置顶部导航栏
        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());

        // 切换预览和编辑模式
        findViewById(R.id.btn_preview).setOnClickListener(v -> {
            isPreviewMode = !isPreviewMode;
            if (isPreviewMode) {
                String markdown = editMarkdown.getText().toString();
                markwon.setMarkdown(textPreview, markdown);
                scrollPreview.setVisibility(View.VISIBLE);
                editMarkdown.setVisibility(View.GONE);
            } else {
                scrollPreview.setVisibility(View.GONE);
                editMarkdown.setVisibility(View.VISIBLE);
            }
        });

        // 发布文章或跳转逻辑
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            // TODO: 你可以在这里添加发布文章或跳转的逻辑
        });
    }

    private void setupMarkdownToolbar() {
        // 设置工具栏按钮
        findViewById(R.id.btn_bold).setOnClickListener(v -> insertMarkdown("**", "**"));
        findViewById(R.id.btn_header).setOnClickListener(v -> insertMarkdown("# ", ""));
        findViewById(R.id.btn_quote).setOnClickListener(v -> insertMarkdown("> ", ""));
        findViewById(R.id.btn_image).setOnClickListener(v -> {
            String imageUrl = "https://yourcdn.com/demo.png"; // TODO: 替换为实际上传的图片地址
            insertMarkdown("![](" + imageUrl + ")", "");
        });
    }

    private void insertMarkdown(String before, String after) {
        // 插入Markdown
        int start = editMarkdown.getSelectionStart();
        int end = editMarkdown.getSelectionEnd();
        Editable editable = editMarkdown.getText();
        editable.replace(start, end, before + editable.subSequence(start, end) + after);
        editMarkdown.setSelection(start + before.length());
    }
}
