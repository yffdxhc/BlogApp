package org.nuist.blogapp.model.entity;

public class WebSocketMessage {
    private String sender;     // 发送者 user_number
    private String receiver;   // 接收者 user_number（私聊用）
    private String data;       // 消息内容
    private String type;       // 消息类型，如 chat、system 等
    private long timestamp;    // 消息发送时间（可用毫秒时间戳表示）

    private User senderInfo;
    private User receiverInfo;

    public WebSocketMessage(String sender, String receiver, String data, String type, long timestamp, User senderInfo, User receiverInfo) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
        this.type = type;
        this.timestamp = timestamp;
        this.senderInfo = senderInfo;
        this.receiverInfo = receiverInfo;
    }

    public WebSocketMessage() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public User getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(User senderInfo) {
        this.senderInfo = senderInfo;
    }

    public User getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(User receiverInfo) {
        this.receiverInfo = receiverInfo;
    }
}


