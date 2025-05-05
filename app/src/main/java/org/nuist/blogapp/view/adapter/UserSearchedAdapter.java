package org.nuist.blogapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.nuist.blogapp.databinding.AdapterUsersSearchedBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.User;

import java.util.List;

public class UserSearchedAdapter extends RecyclerView.Adapter<UserSearchedAdapter.ViewHolder>{
    private final static String TAG = "UserSearchedAdapter";
    private List<User> usersSearched;
    public UserSearchedAdapter(List<User> usersSearched) {
        this.usersSearched = usersSearched;
        Log.d(TAG, "BlogsSearchedAdapter新建对象: "+usersSearched);
    }
    @NonNull
    @Override
    public UserSearchedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterUsersSearchedBinding binding =
                AdapterUsersSearchedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchedAdapter.ViewHolder holder, int position) {
        User user = usersSearched.get(position);
        holder.binding.setUsername(user.getUsername());
        holder.binding.setFollows(user.getFollows()+"粉丝数");
        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl()+user.getAvatar())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.binding.avatar);
    }

    @Override
    public int getItemCount() {
        return usersSearched.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterUsersSearchedBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
