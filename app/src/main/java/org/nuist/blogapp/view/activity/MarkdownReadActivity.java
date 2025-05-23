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

/**
 * 博客阅读页面Activity，主要功能：
 * 1. 展示Markdown格式的博客内容
 * 2. 处理用户互动（点赞/收藏/评论）
 * 3. 同步博客元数据（阅读量/点赞数等）
 */
public class MarkdownReadActivity extends AppCompatActivity {
    private static final String TAG = "MarkdownReadActivity"; // 日志标签

    // ViewModel 用于数据管理
    private BlogViewModel blogViewModel;

    // Markdown渲染器
    private Markwon markwon;

    // UI组件
    private TextView textView;          // 博客内容展示区域
    private TextView textLikeCount;     // 点赞数显示
    private TextView textCollectCount;  // 收藏数显示
    private TextView textCommentCount;  // 评论数显示
    private Button followButton;        // 关注按钮
    private ImageButton likeButton;     // 点赞按钮
    private ImageButton collectButton;  // 收藏按钮
    private ImageButton commentButton;  // 评论按钮
    private ImageButton backButton;     // 返回按钮

    // 状态变量
    private boolean liked = false;      // 当前用户是否已点赞
    private boolean collected = false;  // 当前用户是否已收藏
    private int likeCount = 0;          // 点赞总数
    private int collectCount = 0;       // 收藏总数

    private String blogId;              // 当前展示的博客ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_read);

        // 初始化Markdown渲染器（支持图片加载）
        markwon = Markwon.builder(this)
                .usePlugin(GlideImagesPlugin.create(this))
                .build();

        // 初始化ViewModel
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        initViews();            // 初始化UI组件
        setupClickListeners();  // 设置点击事件

        // 从Intent获取博客ID并加载数据
        blogId = getExtras();
        bindMarkdownContent(blogId);  // 加载博客内容
        bindBlogInfo(blogId);         // 加载博客元数据
    }

    /**
     * 初始化所有UI组件
     */
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

    /**
     * 设置所有按钮的点击事件
     */
    private void setupClickListeners() {
        // 返回按钮
        backButton.setOnClickListener(v -> onBackPressed());

        // 关注按钮（模拟功能）
        followButton.setOnClickListener(v -> {
            if (!checkLogin()) return;  // 登录检查
            String currentText = followButton.getText().toString();
            followButton.setText(currentText.equals("关注") ? "已关注" : "关注");
        });

        // 点赞按钮
        likeButton.setOnClickListener(v -> {
            if (!checkLogin()) return;

            // 更新本地状态
            liked = !liked;
            likeCount = Math.max(0, likeCount + (liked ? 1 : -1));
            updateLikeUI();

            // 向后端发送点赞/取消请求
            blogViewModel.setAndGetLikeButtonResult(blogId).observe(this, aBoolean -> {
                Log.d(TAG, "点赞状态变更: "+aBoolean);
                // 可根据服务器返回结果做进一步处理
            });
        });

        // 收藏按钮
        collectButton.setOnClickListener(v -> {
            if (!checkLogin()) return;

            // 更新本地状态
            collected = !collected;
            collectCount = Math.max(0, collectCount + (collected ? 1 : -1));
            updateCollectUI();

            // 向后端发送收藏/取消请求
            blogViewModel.setAndGetBookMarkButtonResult(blogId).observe(this, aBoolean -> {
                Log.d(TAG, "收藏状态变更: "+aBoolean);
            });
        });

        // 评论按钮（打开底部评论弹窗）
        commentButton.setOnClickListener(v -> {
            if (!checkLogin()) return;
            CommentBottomFragment bottomSheet = new CommentBottomFragment();
            Bundle bundle = new Bundle();
            bundle.putString("blog_id", blogId); // 将 blogId 传入
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "CommentBottomFragment");

        });
    }

    /**
     * 更新点赞按钮的UI状态
     */
    private void updateLikeUI() {
        textLikeCount.setText(String.valueOf(likeCount));
        textLikeCount.setTextColor(liked ? Color.RED : Color.parseColor("#666666"));
        likeButton.setColorFilter(liked ? Color.RED : Color.GRAY);
    }

    /**
     * 更新收藏按钮的UI状态
     */
    private void updateCollectUI() {
        textCollectCount.setText(String.valueOf(collectCount));
        textCollectCount.setTextColor(collected ? Color.RED : Color.parseColor("#666666"));
        collectButton.setColorFilter(collected ? Color.RED : Color.GRAY);
    }

    /**
     * 加载并渲染Markdown格式的博客内容
     * @param blogId 博客ID
     */
    private void bindMarkdownContent(String blogId) {
        blogViewModel.setAndGetBlogContent(blogId).observe(this, markdownText -> {
            // 使用Markwon渲染Markdown内容
            markwon.setMarkdown(textView, markdownText);
        });
    }

    /**
     * 加载博客元数据（点赞数/收藏数/评论数）
     * @param blogId 博客ID
     */
    private void bindBlogInfo(String blogId) {
        // 获取博客基本信息
        blogViewModel.setAndGetBlogInfoByBlogId(blogId).observe(this, blog -> {
            if (blog != null) {
                // 更新计数显示
                likeCount = blog.getLike();
                collectCount = blog.getBookmark();
                int commentCount = blog.getComment();

                textLikeCount.setText(String.valueOf(likeCount));
                textCollectCount.setText(String.valueOf(collectCount));
                textCommentCount.setText(String.valueOf(commentCount));
            }
        });

        // 获取当前用户点赞状态
        blogViewModel.setAndGetIsBlogLike(blogId).observe(this, isLiked -> {
            liked = isLiked;
            updateLikeUI();
        });

        // 获取当前用户收藏状态
        blogViewModel.setAndGetIsBlogMarked(blogId).observe(this, isCollected -> {
            collected = isCollected;
            updateCollectUI();
        });
    }

    /**
     * 从Intent中获取传递的博客ID
     */
    private String getExtras() {
        Bundle bundle = getIntent().getExtras();
        return bundle != null ? bundle.getString("blog_id") : null;
    }

    /**
     * 检查用户登录状态
     * @return true=已登录，false=未登录
     */
    private boolean checkLogin() {
        String token = TokenManager.getInstance().getToken();
        if (token == null || token.trim().isEmpty()) {
            showLoginDialog();  // 显示登录提示
            return false;
        }
        return true;
    }

    /**
     * 显示登录提示对话框
     */
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
