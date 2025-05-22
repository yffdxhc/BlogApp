package org.nuist.blogapp.model.entity;

public class WebSocketMessage {
    private String type;
    private String data;
    private String token;

    public WebSocketMessage(String type, String data, String token) {
        this.type = type;
        this.data = data;
        this.token = token;
    }

    public String getType() { return type; }
    public String getData() { return data; }
    public String getToken() { return token; }

    public void setType(String type) { this.type = type; }
    public void setData(String data) { this.data = data; }
    public void setToken(String token) { this.token = token; }
}
