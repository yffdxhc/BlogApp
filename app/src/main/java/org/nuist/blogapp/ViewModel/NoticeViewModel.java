package org.nuist.blogapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.entity.WebSocketMessage;
import org.nuist.blogapp.websocket.EventDispatcher;
import org.nuist.blogapp.websocket.OnMessage;

/**
 * NoticeViewModel 是一个用于管理系统通知功能的 ViewModel。
 * 它负责处理类型为 "notice" 的 WebSocket 消息，并通过 LiveData 通知 UI 更新。
 */
public class NoticeViewModel extends AndroidViewModel {
    private final String TAG = "NoticeViewModel";

    // 用于存储新通知消息的 LiveData
    private final MutableLiveData<WebSocketMessage> newNotice = new MutableLiveData<>();

    /**
     * 构造函数，初始化 ViewModel 并注册事件监听。
     *
     * @param application 应用程序上下文
     */
    public NoticeViewModel(Application application) {
        super(application);
        EventDispatcher.getInstance().register(this); // 注册 @OnMessage 注解监听
    }

    /**
     * 处理收到的通知消息。
     * 该方法通过 @OnMessage 注解监听 "notice" 类型的消息。
     *
     * @param message 收到的 WebSocketMessage 对象
     */
    @OnMessage("notice")
    public void onNoticeMessage(WebSocketMessage message) {
        Log.d(TAG, "onNoticeMessage: 收到系统通知");
        newNotice.postValue(message); // 更新 LiveData，通知 UI 有新通知
    }

    /**
     * 获取新通知的 LiveData。
     *
     * @return 返回 WebSocketMessage 的 LiveData
     */
    public LiveData<WebSocketMessage> getNewNotice() {
        return newNotice;
    }

    /**
     * 当 ViewModel 被清除时调用，用于释放资源。
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        EventDispatcher.getInstance().unregister(this); // 取消注册事件监听
    }
}
