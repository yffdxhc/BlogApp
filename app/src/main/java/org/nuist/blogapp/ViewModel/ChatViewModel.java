package org.nuist.blogapp.ViewModel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.app.Application;

import org.nuist.blogapp.model.entity.WebSocketMessage;
import org.nuist.blogapp.websocket.OnMessage;
import org.nuist.blogapp.websocket.WsManager;

public class ChatViewModel extends AndroidViewModel {

    private final MutableLiveData<String> newMessage = new MutableLiveData<>();

    public ChatViewModel(Application application) {
        super(application);

        // 注册监听方法
        WsManager.getInstance().connect();
        org.nuist.blogapp.websocket.EventDispatcher.getInstance().register(this);
    }

    @OnMessage("chat")
    public void onChatMessage(WebSocketMessage message) {
        // message.getData() 是 "[userId]: content"
        newMessage.postValue(message.getData());
    }

    public LiveData<String> getNewMessage() {
        return newMessage;
    }

    public void sendMessage(String content) {
        WsManager.getInstance().sendMessage(content);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        org.nuist.blogapp.websocket.EventDispatcher.getInstance().unregister(this);
    }
}
