package org.nuist.blogapp.view.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.nuist.blogapp.databinding.AdapterBlogsByUserBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.activity.MarkdownReadActivity;

import java.util.List;

public class BlogsByUserAdapter extends RecyclerView.Adapter<BlogsByUserAdapter.ViewHolder> {
    private static final String TAG = "BlogsByUserAdapter";
    private List<Blog> blogsByUser;
    public BlogsByUserAdapter(List<Blog> blogsByUser) {
        this.blogsByUser = blogsByUser;
        Log.d(TAG, "BlogsByUserAdapter: "+blogsByUser);
    }
    @NonNull
    @Override
    public BlogsByUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterBlogsByUserBinding binding =
                AdapterBlogsByUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogsByUserAdapter.ViewHolder holder, int position) {
        Blog blog = blogsByUser.get(position);
        holder.binding.setUsername(blog.getUsername());
        holder.binding.setTitle(blog.getBlog_title());
        holder.binding.setCollectNumber(String.valueOf(blog.getBookmark()));
        holder.binding.setTime(String.valueOf(blog.getCreate_time()));
        holder.binding.setLikeNumber(String.valueOf(blog.getLike()));
        holder.binding.setVisitNumber(String.valueOf(blog.getVisit()));

        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+blog.getUser_avatar())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.blogItemUserAvatar);
        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+blog.getCover_image())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.blogItemCover);

        holder.binding.blogByUserNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MarkdownReadActivity.class);
                intent.putExtra("blog_id",blog.getBlog_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsByUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterBlogsByUserBinding  binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
