package org.nuist.blogapp.model.entity;

public class WebSocketMessage {
    private String type;
    private String data;

    public WebSocketMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() { return type; }
    public String getData() { return data; }

    public void setType(String type) { this.type = type; }
    public void setData(String data) { this.data = data; }
}
