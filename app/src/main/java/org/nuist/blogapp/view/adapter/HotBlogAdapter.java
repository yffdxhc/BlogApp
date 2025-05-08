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

import org.nuist.blogapp.databinding.AdapterHotBlogBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.activity.MarkdownReadActivity;

import java.util.List;

public class HotBlogAdapter extends RecyclerView.Adapter<HotBlogAdapter.ViewHolder>{
    private final static String TAG = "HotBlogAdapter";
    private List<Blog> hotBlogs;

    public HotBlogAdapter(List<Blog> hotBlogs) {
        this.hotBlogs = hotBlogs;
        Log.d(TAG, "HotBlogAdapter: "+hotBlogs);
    }

    @NonNull
    @Override
    public HotBlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterHotBlogBinding binding =
                AdapterHotBlogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotBlogAdapter.ViewHolder holder, int position) {
        Blog blog = hotBlogs.get(position);
        holder.binding.setUsername(blog.getUsername());
        holder.binding.setTitle(blog.getBlog_title());
        holder.binding.setCollectNumber(String.valueOf(blog.getBookmark()));
        holder.binding.setRank(String.valueOf(position+1));
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

        holder.binding.blogHot.setOnClickListener(new View.OnClickListener() {
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
        return hotBlogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterHotBlogBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
