package org.nuist.blogapp.view.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.nuist.blogapp.databinding.AdapterChatObjectBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.activity.ChatActivity;

import java.util.List;

public class ChatObjectAdapter extends RecyclerView.Adapter<ChatObjectAdapter.ViewHolder> {

    private static final String TAG = "ChatObjectAdapter";
    private List<User> chatList;

    public ChatObjectAdapter(List<User> chatList) {
        this.chatList = chatList;
        Log.d(TAG, "ChatObjectAdapter initialized with " + chatList.size() + " chats");
    }
    public void setData(List<User> newChatList) {
        chatList.clear();
        chatList.addAll(newChatList);
        Log.d(TAG, "ChatObjectAdapter initialized with " + chatList.size() + " chats");
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatObjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterChatObjectBinding binding = AdapterChatObjectBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding = binding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatObjectAdapter.ViewHolder holder, int position) {
        User chat = chatList.get(position);
        holder.binding.setUsername(chat.getUsername());
        Log.d(TAG, "onBindViewHolder: "+chat.getUsername());
/*        holder.binding.setLastMessage(chat.getLastMessage());
        holder.binding.setMessageTime(chat.getMessageTime());*/

        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl() + chat.getAvatar())
                .circleCrop()
                .into(holder.binding.avatar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra("user_number", chat.getUser_id());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList != null ? chatList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterChatObjectBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
