package org.nuist.blogapp.model.entity;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private final static long SerialVersionUID= 1L;
    private boolean success;
    private String message;
    private T data;

    public Result() {
    }
    // 构造函数、getter 和 setter 方法

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
