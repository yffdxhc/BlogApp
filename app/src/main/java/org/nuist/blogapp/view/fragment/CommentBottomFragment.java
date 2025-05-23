package org.nuist.blogapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.model.entity.Comment;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.CommentAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentBottomFragment extends BottomSheetDialogFragment {

    private static final String TAG = "CommentBottomFragment";

    private RecyclerView recyclerView;
    private EditText editTextComment;
    private Button buttonSend;
    private BlogViewModel blogViewModel;

    private final List<Comment> comments = new ArrayList<>();
    private CommentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView called");

        View view = inflater.inflate(R.layout.fragment_comment_bottom, container, false);

        blogViewModel = new ViewModelProvider(requireActivity()).get(BlogViewModel.class);
        Log.d(TAG, "ViewModel acquired");

        recyclerView = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonSend = view.findViewById(R.id.button_send);
        Log.d(TAG, "Views initialized");

        adapter = new CommentAdapter(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        String blogId = getArguments() != null ? getArguments().getString("blog_id") : null;
        Log.d(TAG, "blog_id = " + blogId);

        blogViewModel.setAndGetComments(blogId)
                .observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> newComments) {
                        Log.d(TAG, "onChanged: " + newComments);
                        comments.clear();
                        if (newComments != null) {
                            comments.addAll(newComments);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        buttonSend.setOnClickListener(v -> {
            String text = editTextComment.getText().toString().trim();
            if (!text.isEmpty()) {
                Comment newComment = new Comment();
                newComment.setBlog_id(blogId);
                newComment.setContent(text);
                newComment.setCreated_at(new Timestamp(new Date().getTime()));
                newComment.setUser_number("2025123456"); // 可从登录信息中获取当前用户学号

                User user = new User();
                user.setUser_number("用户昵称"); // 设置昵称
                user.setAvatar("https://example.com/avatar.jpg"); // 设置头像

                newComment.setUser(user);

                comments.add(0, newComment);
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                editTextComment.setText("");
                hideKeyboard();
                Log.d(TAG, "New comment added: " + text);
            }
        });

        return view;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            parent.getLayoutParams().height = getScreenHeight();
            parent.requestLayout();
            Log.d(TAG, "Bottom sheet height adjusted");
        }
    }

    private int getScreenHeight() {
        return (int) (requireContext().getResources().getDisplayMetrics().heightPixels * 0.8);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null && view.getParent() instanceof View) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
            behavior.setDraggable(false); // 可选：禁止拖动
            Log.d(TAG, "Bottom sheet expanded");
        }
    }
}
