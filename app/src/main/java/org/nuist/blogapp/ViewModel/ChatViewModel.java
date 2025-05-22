package org.nuist.blogapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.entity.WebSocketMessage;
import org.nuist.blogapp.websocket.EventDispatcher;
import org.nuist.blogapp.websocket.OnMessage;
import org.nuist.blogapp.websocket.WsManager;

/**
 * ChatViewModel 是一个用于管理聊天功能的 ViewModel。
 * 它负责处理 WebSocket 消息的接收和发送，并通过 LiveData 通知 UI 更新。
 */
public class ChatViewModel extends AndroidViewModel {
    private final String TAG = "ChatViewModel";

    // 用于存储新消息的 MutableLiveData，UI 可以通过观察它来获取新消息
    private final MutableLiveData<WebSocketMessage> newMessage = new MutableLiveData<>();

    /**
     * 构造函数，初始化 ViewModel。
     *
     * @param application 应用程序上下文
     */
    public ChatViewModel(Application application) {
        super(application);
        EventDispatcher.getInstance().register(this); // 注册 @OnMessage 注解监听
    }

    /**
     * 处理收到的聊天消息。
     * 该方法通过 @OnMessage 注解监听 "chat" 类型的消息。
     *
     * @param message 收到的 WebSocketMessage 对象
     */
    @OnMessage("chat")
    public void onChatMessage(WebSocketMessage message) {
        Log.d(TAG, "onChatMessage: ");
        // 不是这里判断，后端已经根据发送者userNumber发送给了发送者和接收者
/*        String myToken = TokenManager.getInstance().getToken(); // 获取当前用户的 token
        // 如果是私聊且接收者不是我，就忽略
        if (message.getReceiver() != null && !message.getReceiver().equals(myToken)) {
            return;
        }*/

        newMessage.postValue(message); // 更新 LiveData，通知 UI 有新消息
    }

    /**
     * 获取新消息的 LiveData。
     *
     * @return 返回 WebSocketMessage 的 LiveData
     */
    public LiveData<WebSocketMessage> getNewMessage() {
        return newMessage;
    }

    /**
     * 发送聊天消息。
     * 该方法用于发送群聊消息，接收者为空。
     *
     * @param content 消息内容
     */
    public void sendMessage(String content) {
        String token = TokenManager.getInstance().getToken(); // 获取当前用户的 token
        WebSocketMessage message = new WebSocketMessage();
        message.setSender(token); // 设置发送者
        message.setReceiver(""); // 设置接收者为空（群聊）
        message.setData(content); // 设置消息内容
        message.setType("chat"); // 设置消息类型
        message.setTimestamp(System.currentTimeMillis()); // 设置时间戳
        WsManager.getInstance().sendMessage(message); // 发送消息
    }

    /**
     * 发送私聊消息。
     * 该方法用于发送私聊消息，指定接收者的用户编号。
     *
     * @param content  消息内容
     * @param receiver 接收者的用户编号
     */
    public void sendPrivateMessage(String content, String receiver) {
        String token = TokenManager.getInstance().getToken(); // 获取当前用户的 token
        WebSocketMessage message = new WebSocketMessage();
        message.setReceiver(receiver); // 设置接收者
        message.setData(content); // 设置消息内容
        message.setType("chat"); // 设置消息类型
        WsManager.getInstance().sendMessage(message); // 发送消息
    }

    /**
     * 当 ViewModel 被清除时调用，用于释放资源。
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        EventDispatcher.getInstance().unregister(this); // 取消注册事件监听，避免内存泄漏
    }
}
