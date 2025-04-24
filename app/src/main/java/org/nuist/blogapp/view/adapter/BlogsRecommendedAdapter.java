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

import org.nuist.blogapp.databinding.AdapterBlogsRecommendedBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.activity.MarkdownReadActivity;

import java.util.List;

public class BlogsRecommendedAdapter extends RecyclerView.Adapter<BlogsRecommendedAdapter.ViewHolder>{
    private String TAG = "BlogsRecommendedAdapter";
    private List<Blog> blogsRecommended;

    public BlogsRecommendedAdapter(List<Blog> blogsRecommended) {
        this.blogsRecommended = blogsRecommended;
        Log.d(TAG, "BlogsRecommendedAdapter: 有参构造，获取的推荐书籍: "+blogsRecommended);
    }
    @NonNull
    @Override
    public BlogsRecommendedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 创建ViewHolder");
        AdapterBlogsRecommendedBinding binding =
                AdapterBlogsRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogsRecommendedAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 绑定ViewHolder");
        Blog blog = blogsRecommended.get(position);
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
        holder.binding.blogRecommended.setOnClickListener(new View.OnClickListener() {
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
        Log.d(TAG, "getItemCount: 获取数据长度:"+blogsRecommended.size());
        return blogsRecommended.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterBlogsRecommendedBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
