package org.nuist.blogapp.view.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.view.fragment.CommentBottomFragment;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class MarkdownReadActivity extends AppCompatActivity {
    private static final String TAG = "MarkdownReadActivity";

    private BlogViewModel blogViewModel;
    private Markwon markwon;

    private TextView textView;
    private TextView textLikeCount;
    private TextView textCollectCount;
    private TextView textCommentCount;
    private Button followButton;
    private ImageButton likeButton;
    private ImageButton collectButton;
    private ImageButton commentButton;
    private ImageButton backButton;

    private boolean liked = false;
    private boolean collected = false;
    private int likeCount = 0;
    private int collectCount = 0;

    private String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_read);

        markwon = Markwon.builder(this)
                .usePlugin(GlideImagesPlugin.create(this))
                .build();

        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        initViews();
        setupClickListeners();

        blogId = getExtras();
        bindMarkdownContent(blogId);
        bindBlogInfo(blogId);
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
        textCommentCount = findViewById(R.id.text_comment_count);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> onBackPressed());

        followButton.setOnClickListener(v -> {
            if (!checkLogin()) return;
            String currentText = followButton.getText().toString();
            followButton.setText(currentText.equals("关注") ? "已关注" : "关注");
        });

        likeButton.setOnClickListener(v -> {
            if (!checkLogin()) return;
            liked = !liked;
            likeCount = Math.max(0, likeCount + (liked ? 1 : -1));
            updateLikeUI();

            // TODO: 向后端发送点赞/取消请求
            blogViewModel.setAndGetLikeButtonResult(blogId).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    Log.d(TAG, "onChanged: "+aBoolean);
                }
            });
        });

        collectButton.setOnClickListener(v -> {
            if (!checkLogin()) return;
            collected = !collected;
            collectCount = Math.max(0, collectCount + (collected ? 1 : -1));
            updateCollectUI();

            // TODO: 向后端发送收藏/取消请求
            // blogViewModel.collectBlog(blogId, collected);
        });

        commentButton.setOnClickListener(v -> {
            if (!checkLogin()) return;
            CommentBottomFragment bottomSheet = new CommentBottomFragment();
            bottomSheet.show(getSupportFragmentManager(), "CommentBottomFragment");
        });
    }

    private void updateLikeUI() {
        textLikeCount.setText(String.valueOf(likeCount));
        textLikeCount.setTextColor(liked ? Color.RED : Color.parseColor("#666666"));
        likeButton.setColorFilter(liked ? Color.RED : Color.GRAY);
    }

    private void updateCollectUI() {
        textCollectCount.setText(String.valueOf(collectCount));
        textCollectCount.setTextColor(collected ? Color.RED : Color.parseColor("#666666"));
        collectButton.setColorFilter(collected ? Color.RED : Color.GRAY);
    }

    private void bindMarkdownContent(String blogId) {
        blogViewModel.setAndGetBlogContent(blogId).observe(this, markdownText -> {
            markwon.setMarkdown(textView, markdownText);
        });
    }

    private void bindBlogInfo(String blogId) {
        blogViewModel.setAndGetBlogInfoByBlogId(blogId).observe(this, blog -> {
            if (blog != null) {
                likeCount = blog.getLike();
                collectCount = blog.getBookmark();
                int commentCount = blog.getComment();

                textLikeCount.setText(String.valueOf(likeCount));
                textCollectCount.setText(String.valueOf(collectCount));
                textCommentCount.setText(String.valueOf(commentCount));
            }
        });

        blogViewModel.setAndGetIsBlogLike(blogId).observe(this, isLiked -> {
            liked = isLiked;
            updateLikeUI();
        });


        blogViewModel.setAndGetIsBlogMarked(blogId).observe(this, isCollected -> {
            collected = isCollected;
            updateCollectUI();
        });
    }

    private String getExtras() {
        Bundle bundle = getIntent().getExtras();
        return bundle != null ? bundle.getString("blog_id") : null;
    }

    private boolean checkLogin() {
        String token = TokenManager.getInstance().getToken();
        if (token == null || token.trim().isEmpty()) {
            showLoginDialog();
            return false;
        }
        return true;
    }

    private void showLoginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您还未登录，是否前往登录？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // TODO: 跳转登录页
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
