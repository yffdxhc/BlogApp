package org.nuist.blogapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.nuist.blogapp.R;
import org.nuist.blogapp.custom.CircleImageView;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.WebSocketMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatAdapter 是一个 RecyclerView 的适配器，用于显示聊天消息列表。
 * 它根据消息的发送者是否为当前用户来调整消息的显示样式。
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private String TAG = "ChatAdapter"; // 日志标签

    private final List<WebSocketMessage> messages = new ArrayList<>(); // 存储聊天消息的列表
    public static String userNumber="";// 当前用户编号

    /**
     * 构造函数，初始化 ChatAdapter。
     *
     */
    public ChatAdapter() {
    }

    /**
     * 创建 ViewHolder 实例。
     *
     * @param parent   父视图组
     * @param viewType 视图类型
     * @return 返回一个新的 ChatViewHolder 实例
     */
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    /**
     * 绑定数据到 ViewHolder。
     *
     * @param holder   ViewHolder 实例
     * @param position 数据项的位置
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        WebSocketMessage message = messages.get(position);
        boolean isMe = userNumber.equals(message.getSender());

        // 设置显示文本
        String sender = isMe ? "我" : message.getSender();
        //holder.messageSender.setText(sender);
        holder.messageContent.setText(message.getData());

        ConstraintLayout layout = (ConstraintLayout) holder.itemView;
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        if (isMe) {
            // 当前用户：头像靠右，消息靠左对其头像
            set.clear(R.id.user_avatar, ConstraintSet.START);
            set.connect(R.id.user_avatar, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

            set.clear(R.id.messageContent, ConstraintSet.START);
            set.connect(R.id.messageContent, ConstraintSet.END, R.id.user_avatar, ConstraintSet.START);
            set.setHorizontalBias(R.id.messageContent, 1f); // 向右偏移
            holder.messageContent.setBackgroundResource(R.drawable.bg_message_sent);
        } else {
            // 对方消息：头像靠左，消息靠右对其头像
            set.clear(R.id.user_avatar, ConstraintSet.END);
            set.connect(R.id.user_avatar, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);

            set.clear(R.id.messageContent, ConstraintSet.END);
            set.connect(R.id.messageContent, ConstraintSet.START, R.id.user_avatar, ConstraintSet.END);
            set.setHorizontalBias(R.id.messageContent, 0f); // 向左偏移
            holder.messageContent.setBackgroundResource(R.drawable.bg_message_received);
        }
        set.applyTo(layout);


// 始终使用发送者的头像
        String avatarUrl = message.getSenderInfo() != null ? message.getSenderInfo().getAvatar() : "";

        Glide.with(holder.itemView.getContext())
                .load(RetrofitClient.getBaseUrl() + avatarUrl)
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .into(holder.user_avatar);




/*        // 设置背景和颜色
        if (isMe) {
            holder.messageContent.setBackgroundResource(R.drawable.bg_message_sent);
            //holder.messageSender.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            // 将消息容器对齐到右边
            ((LinearLayout) holder.itemView).setGravity(Gravity.END);
        } else {
            holder.messageContent.setBackgroundResource(R.drawable.bg_message_received);
            //holder.messageSender.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            // 将消息容器对齐到左边
            ((LinearLayout) holder.itemView).setGravity(Gravity.START);
        }*/
    }


    /**
     * 获取数据项的数量。
     *
     * @return 返回消息列表的大小
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * 添加一条新消息到消息列表，并通知适配器更新。
     *
     * @param message 要添加的 WebSocketMessage 对象
     */
    public void addMessage(WebSocketMessage message) {
        Log.d(TAG, "addMessage: "+message);
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    /**
     * ChatViewHolder 是 RecyclerView.ViewHolder 的子类，用于持有聊天消息的视图。
     */
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        //TextView messageSender; // 显示消息发送者的 TextView
        TextView messageContent; // 显示消息内容的 TextView
        CircleImageView user_avatar;

        /**
         * 构造函数，初始化 ViewHolder。
         *
         * @param itemView 视图项
         */
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            //messageSender = itemView.findViewById(R.id.messageSender);
            messageContent = itemView.findViewById(R.id.messageContent);
            user_avatar = itemView.findViewById(R.id.user_avatar);
        }
    }
}
