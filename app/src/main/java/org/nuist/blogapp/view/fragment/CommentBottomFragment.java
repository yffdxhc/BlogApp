package org.nuist.blogapp.view.fragment;

import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.model.entity.Comment;
import org.nuist.blogapp.view.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommentBottomFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private EditText editTextComment;
    private Button buttonSend;

    private final List<Comment> comments = new ArrayList<>();
    private CommentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_bottom, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonSend = view.findViewById(R.id.button_send);

        adapter = new CommentAdapter(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> {
            String text = editTextComment.getText().toString().trim();
            if (!text.isEmpty()) {
                comments.add(0, new Comment(
                        "https://example.com/avatar.jpg", // 替换为真实头像链接或本地资源路径
                        "用户昵称",
                        text
                ));

                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                editTextComment.setText("");
                hideKeyboard();
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
        }
    }

}
