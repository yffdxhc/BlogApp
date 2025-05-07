package org.nuist.blogapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.view.fragment.CommentBottomFragment;

import io.noties.markwon.Markwon;

public class MarkdownReadActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private Markwon markwon;

    private TextView textView;
    private TextView textLikeCount;
    private TextView textCollectCount;
    private Button followButton;
    private ImageButton likeButton;
    private ImageButton collectButton;
    private ImageButton commentButton;
    private ImageButton backButton;

    private boolean liked = false;
    private boolean collected = false;
    private int likeCount = 23;
    private int collectCount = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_read);

        markwon = Markwon.create(this);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        initViews();
        setupClickListeners();

        String blogId = getExtras();
        bindMarkdownContent(blogId);
    }

    private void initViews() {
        textView = findViewById(R.id.text_view_result);
        followButton = findViewById(R.id.action_button);
        likeButton = findViewById(R.id.button_like);
        collectButton = findViewById(R.id.button_collect);
        commentButton = findViewById(R.id.button_comment);
        textLikeCount = findViewById(R.id.text_like_count);
        textCollectCount = findViewById(R.id.text_collect_count);
        backButton = findViewById(R.id.back_button);
    }

    private void setupClickListeners() {
        // 返回按钮
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        // 关注按钮
        followButton.setOnClickListener(v -> {
            String currentText = followButton.getText().toString();
            followButton.setText(currentText.equals("关注") ? "已关注" : "关注");
        });

        // 点赞按钮
        likeButton.setOnClickListener(v -> {
            liked = !liked;
            likeCount += liked ? 1 : -1;
            textLikeCount.setText(String.valueOf(likeCount));
            textLikeCount.setTextColor(liked ? Color.RED : Color.parseColor("#666666"));
            likeButton.setColorFilter(liked ? Color.RED : Color.GRAY);
        });

        // 收藏按钮
        collectButton.setOnClickListener(v -> {
            collected = !collected;
            collectCount += collected ? 1 : -1;
            textCollectCount.setText(String.valueOf(collectCount));
            textCollectCount.setTextColor(collected ? Color.RED : Color.parseColor("#666666"));
            collectButton.setColorFilter(collected ? Color.RED : Color.GRAY);
        });

        // 评论按钮
        commentButton.setOnClickListener(v -> {
            CommentBottomFragment bottomSheet = new CommentBottomFragment();
            bottomSheet.show(getSupportFragmentManager(), "CommentBottomFragment");
        });
    }

    // 绑定Markdown内容
    private void bindMarkdownContent(String blogId) {
        blogViewModel.setAndGetBlogContent(blogId).observe(this, markdownText -> {
            markwon.setMarkdown(textView, markdownText);
        });
    }

    // 获取传递过来的数据
    private String getExtras() {
        Bundle bundle = getIntent().getExtras();
        return bundle != null ? bundle.getString("blog_id") : null;
    }
}

