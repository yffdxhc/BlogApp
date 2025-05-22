package org.nuist.blogapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.nuist.blogapp.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        Intent  intent = getIntent();
        String userNumber = intent.getStringExtra("user_number");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView text = findViewById(R.id.textView);
        text.setText(userNumber);
    }
}