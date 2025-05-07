package org.nuist.blogapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.nuist.blogapp.R;
import org.nuist.blogapp.model.entity.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> comments) {
        this.commentList = comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nicknameText;
        TextView contentText;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.image_avatar);
            nicknameText = itemView.findViewById(R.id.text_nickname);
            contentText = itemView.findViewById(R.id.text_content);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.nicknameText.setText(comment.getNickname());
        holder.contentText.setText(comment.getContent());

        // 使用 Glide 加载头像
        Glide.with(holder.avatarImage.getContext())
                .load(comment.getAvatarUrl())
                .placeholder(R.drawable.aln2) // 你可以换成默认头像
                .circleCrop()
                .into(holder.avatarImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
