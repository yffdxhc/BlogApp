package org.nuist.blogapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.nuist.blogapp.R;
import org.nuist.blogapp.util.MarkwonHelper;

import io.noties.markwon.Markwon;

public class MarkdownEditorActivity extends AppCompatActivity {

    private EditText editMarkdown;
    private TextView textPreview;
    private ScrollView scrollPreview;
    private LinearLayout markdownToolbar;// 工具栏
    private Toolbar markdownEditorToolbar;//状态栏
    private boolean isPreviewMode = false;// 预览状态
    private TextView btnPreview;
    private Markwon markwon;

    private final int NEW_BLOG = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_editor);
        setSupportActionBar(markdownEditorToolbar);

        // === 1. 初始化视图和 Markwon 实例 ===
        editMarkdown = findViewById(R.id.edit_markdown);
        textPreview = findViewById(R.id.text_preview);
        scrollPreview = findViewById(R.id.scroll_preview);
        markdownToolbar = findViewById(R.id.markdown_toolbar);
        markdownEditorToolbar = findViewById(R.id.markdown_editor_toolbar);
        btnPreview = findViewById(R.id.btn_preview);
        markwon = MarkwonHelper.getInstance(this);

        // === 2. 添加监听器，自动保存输入内容为草稿 ===
        editMarkdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSharedPreferences("markdown_draft", MODE_PRIVATE)
                        .edit().putString("draft_text", s.toString()).apply(); // 保存草稿
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // === 3. 恢复之前保存的草稿内容 ===
        String savedText = getSharedPreferences("markdown_draft", MODE_PRIVATE)
                .getString("draft_text", "");
        editMarkdown.setText(savedText);

        // === 4. 设置顶部栏和 Markdown 编辑工具栏 ===
        setupTopBar();
        setupMarkdownToolbar();
    }

    /**
     * 设置顶部导航栏的事件逻辑
     */
    private void setupTopBar() {
        // toolbar返回
        markdownEditorToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 关闭按钮，结束当前 Activity
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("提示")
                        .setMessage("确定要清空内容并退出吗？草稿将被删除。")
                        .setPositiveButton("确定", (dialog, which) -> {
                            // 清空草稿并退出
                            getSharedPreferences("markdown_draft", MODE_PRIVATE)
                                    .edit().remove("draft_text").apply(); // 删除草稿
                            finish();
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 预览模式切换按钮
        findViewById(R.id.btn_preview).setOnClickListener(v -> {
            isPreviewMode = !isPreviewMode;
            if (isPreviewMode) {
                // 切换为预览模式：隐藏编辑框，显示渲染结果
                String markdown = editMarkdown.getText().toString();
                markwon.setMarkdown(textPreview, markdown);
                scrollPreview.setVisibility(View.VISIBLE);
                editMarkdown.setVisibility(View.GONE);
                markdownToolbar.setVisibility(View.GONE);
                btnPreview.setText("编辑");
            } else {
                // 返回编辑模式：显示编辑框和工具栏
                scrollPreview.setVisibility(View.GONE);
                editMarkdown.setVisibility(View.VISIBLE);
                markdownToolbar.setVisibility(View.VISIBLE);
                btnPreview.setText("预览");
            }
        });

        // 下一步或发布按钮（可扩展）
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            String content = editMarkdown.getText().toString().trim();
            if (content.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("内容不能为空，请输入后再继续。")
                        .setPositiveButton("确定", null)
                        .show();
            } else {
                Intent intent = new Intent(this, BlogReleaseActivity.class);
                intent.putExtra("content", NEW_BLOG);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * 设置 Markdown 工具栏按钮的事件
     */
    private void setupMarkdownToolbar() {
        // 加粗按钮：插入 **
        findViewById(R.id.btn_bold).setOnClickListener(v -> insertMarkdown("**", "**"));

        // 标题按钮：插入 #
        findViewById(R.id.btn_header).setOnClickListener(v -> insertMarkdown("# ", ""));

        // 引用按钮：插入 >
        findViewById(R.id.btn_quote).setOnClickListener(v -> insertMarkdown("> ", ""));

        // 插入图片按钮：插入 ![](url)
        findViewById(R.id.btn_image).setOnClickListener(v -> {
            String imageUrl = "https://yourcdn.com/demo.png"; // TODO: 替换为实际上传后的图片地址
            insertMarkdown("![](" + imageUrl + ")", "");
        });
    }

    /**
     * 将 Markdown 语法插入到当前光标位置
     *
     * @param before 插入前缀（如 `**`）
     * @param after  插入后缀（如 `**`）
     */
    private void insertMarkdown(String before, String after) {
        int start = editMarkdown.getSelectionStart();
        int end = editMarkdown.getSelectionEnd();
        Editable editable = editMarkdown.getText();
        editable.replace(start, end, before + editable.subSequence(start, end) + after);

        // 设置光标位置，方便继续输入
        editMarkdown.setSelection(start + before.length());
    }
}
