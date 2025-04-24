package org.nuist.blogapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;

import io.noties.markwon.Markwon;

public class MarkdownReadActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_read);

        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
        editText = findViewById(R.id.edit_text_markdown);
        textView = findViewById(R.id.text_view_result);
        Button button = findViewById(R.id.button_render);

        //获取Bundle里的数据
        String blog_id = getExtras();
        onBindView(blog_id);

        Markwon markwon = Markwon.create(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String markdown = editText.getText().toString();
                markwon.setMarkdown(textView, markdown);
            }
        });
    }

    private String getExtras(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getString("blog_id");
        }
        return null;
    }

    private void onBindView(String blog_id){
        blogViewModel.setAndGetBlogContent(blog_id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editText.setText(s);
            }
        });
    }
}