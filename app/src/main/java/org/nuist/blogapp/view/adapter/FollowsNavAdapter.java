package org.nuist.blogapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.nuist.blogapp.R;
import org.nuist.blogapp.custom.CircleImageView;
import org.nuist.blogapp.databinding.AdapterFollowsNavBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.User;

import java.util.List;

public class FollowsNavAdapter extends RecyclerView.Adapter<FollowsNavAdapter.ViewHolder>{
    private final static String TAG = "FollowsNavAdapter";
    private List<User> follows;
    // 回调
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    public FollowsNavAdapter(List<User> follows) {
        this.follows = follows;
        Log.d(TAG, "FollowsNavAdapter: "+follows);
    }

    @NonNull
    @Override
    public FollowsNavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterFollowsNavBinding binding =
                AdapterFollowsNavBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowsNavAdapter.ViewHolder holder, int position) {
        User user = follows.get(position);
        holder.binding.setUsername(user.getUsername());
        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+user.getAvatar())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.ivAvatar);
        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return follows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterFollowsNavBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
