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

import org.nuist.blogapp.databinding.AdapterBlogsSearchedBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.activity.MarkdownReadActivity;

import java.util.List;

public class BlogsSearchedAdapter extends RecyclerView.Adapter<BlogsSearchedAdapter.ViewHolder> {
    private final static String TAG = BlogsSearchedAdapter.class.getSimpleName();
    private List<Blog> blogsSearched;

    public BlogsSearchedAdapter(List<Blog> blogsSearched) {
        this.blogsSearched = blogsSearched;
        Log.d(TAG, "BlogsSearchedAdapter新建对象: "+blogsSearched);
    }
    @NonNull
    @Override
    public BlogsSearchedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterBlogsSearchedBinding binding =
                AdapterBlogsSearchedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogsSearchedAdapter.ViewHolder holder, int position) {
        Blog blog = blogsSearched.get(position);
        holder.binding.setUsername(blog.getUsername());
        holder.binding.setTitle(blog.getBlog_title());
        holder.binding.setSummary(blog.getBlog_summary());
        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+blog.getUser_avatar())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.blogItemUserAvatar);
        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+blog.getCover_image())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.blogItemCover);
        holder.binding.blogSearched.setOnClickListener(new View.OnClickListener() {
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
        Log.d(TAG, "getItemCount: 获取数据长度:"+blogsSearched.size());
        return blogsSearched.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterBlogsSearchedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
