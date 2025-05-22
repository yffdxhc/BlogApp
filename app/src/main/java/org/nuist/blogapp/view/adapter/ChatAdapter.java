package org.nuist.blogapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<String> messages = new ArrayList<>();

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);

        // 判断是否含有 [xxx] 前缀
        if (message.startsWith("[")) {
            int endIndex = message.indexOf("]");
            if (endIndex != -1) {
                String sender = message.substring(0, endIndex + 1);  // 包括中括号
                String content = message.substring(endIndex + 1).trim();

                holder.messageSender.setText(sender);
                holder.messageContent.setText(content);
            } else {
                // 没有正确格式就整体显示
                holder.messageSender.setText("[未知]");
                holder.messageContent.setText(message);
            }
        } else {
            holder.messageSender.setText("[未知]");
            holder.messageContent.setText(message);
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(String message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageSender;
        TextView messageContent;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSender = itemView.findViewById(R.id.messageSender);
            messageContent = itemView.findViewById(R.id.messageContent);
        }
    }
}

