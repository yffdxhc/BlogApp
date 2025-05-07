package org.nuist.blogapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.util.FileUtil;

import java.io.File;
import java.nio.file.Path;

public class BlogReleaseActivity extends AppCompatActivity {
    private static final String TAG = "BlogReleaseActivity";

    private Toolbar blogReleaseToolbar;
    private EditText blogTitleEditText;
    private ImageView image_cover;
    private File coverImagePath;// 图片路径
    private String blogContent;
    private EditText blogSummaryEditText;
    private RadioGroup blogTypeRadioGroup;
    private Button blogReleaseButton;
    private Button blogSaveButton;
    private BlogViewModel blogViewModel;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_release);

        blogReleaseToolbar = findViewById(R.id.blog_release_toolbar);
        blogTitleEditText = findViewById(R.id.edit_title);
        blogSummaryEditText = findViewById(R.id.edit_summary);
        blogTypeRadioGroup = findViewById(R.id.radio_group_type);
        blogReleaseButton = findViewById(R.id.btn_release);
        blogSaveButton = findViewById(R.id.btn_save);
        image_cover = findViewById(R.id.image_cover);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        Intent  intent = getIntent();
        if (intent.getIntExtra("content", -1) == 20000) {
            String savedText = getSharedPreferences("markdown_draft", MODE_PRIVATE)
                    .getString("draft_text", "");
            blogContent  = savedText;
        }

        setListener();
    }

    private void setListener() {
        // 返回按钮
        blogReleaseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 图片选择
        image_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转文件选择器
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //  发布按钮
        blogReleaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedType = 0; // 默认原创
                int checkedId = blogTypeRadioGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.radio_original) {
                    selectedType = 0;
                } else if (checkedId == R.id.radio_reprint) {
                    selectedType = 1;
                } else if (checkedId == R.id.radio_translation) {
                    selectedType = 2;
                }

                blogViewModel.setAndGetBlogReleaseResult(
                        blogTitleEditText.getText().toString(),
                        blogContent,
                        blogSummaryEditText.getText().toString(),
                        selectedType,
                        coverImagePath
                ).observe(BlogReleaseActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d(TAG, "博客发布状态: " + aBoolean);
                        if (aBoolean) {
                            getSharedPreferences("markdown_draft", MODE_PRIVATE)
                                    .edit().remove("draft_text").apply(); // 删除草稿
                            finish();
                            new AlertDialog.Builder(BlogReleaseActivity.this)
                                    .setTitle("发布成功")
                                    .setMessage("您的博客已经发布成功")
                                    .setPositiveButton("确定", null)
                                    .show();
                        } else {
                            new AlertDialog.Builder(BlogReleaseActivity.this)
                                    .setTitle("发布失败")
                                    .setMessage("您的博客发布失败")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 检查请求码和结果码是否匹配预期值
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 检查返回的数据是否为空
            if (data != null) {
                Uri selectedImageUri = data.getData();
                Log.d(TAG, "封面图片uri: " + selectedImageUri);
                String path = FileUtil.getPathFromSAFUri(selectedImageUri,getApplicationContext());
                Log.d(TAG, "获取图片路径: "+path);
                assert path != null;
                coverImagePath = new File(path);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    image_cover.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "加载图片时发生异常: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "封面选择为空");
            }
        }
    }
}