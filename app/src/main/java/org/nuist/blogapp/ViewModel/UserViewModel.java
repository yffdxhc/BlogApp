package org.nuist.blogapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.model.repository.UserRepository;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class UserViewModel extends AndroidViewModel {
    private static final String TAG = "UserViewModel";

    private UserRepository userRepository;
    private MutableLiveData<String> tokenLiveData;
    private MutableLiveData<List<User>> usersSearchedLiveData;
    private MutableLiveData<Boolean> testLiveData;
    private MutableLiveData<List<User>> usersFollowsLiveData;
    private MutableLiveData<User> userInfoLiveData;
    private MutableLiveData<List<User>> chatObjectLiveData;
    private TokenManager tokenManager;

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        tokenManager = new TokenManager(application.getApplicationContext());
        tokenLiveData = new MutableLiveData<>();
    }

    /**
     * 向服务器发起请求，登录获取Token
     * @param partMap
     * @return
     */
    public MutableLiveData<String> setToken(Map<String, RequestBody> partMap) {
        Log.d(TAG, "setToken: ");
        tokenLiveData = userRepository.loginId(partMap);
        return tokenLiveData;
    }

    /**
     * 获取token
     * @return null或token
     */
    public LiveData<String> getToken() {
        String token = tokenManager.getToken();
        tokenLiveData.postValue(token);
        return tokenLiveData;
    }
    public LiveData<List<User>> setAndGetUsersSearched(String query) {
        Log.d(TAG, "setAndGetUsersSearched: ");
        usersSearchedLiveData = userRepository.getUserSearched(query);
        return usersSearchedLiveData;
    }

    public LiveData<Boolean> setAndGetTestResult() {
        testLiveData = userRepository.test();
        Log.d(TAG, "setAndGetTestResult: ");
        return testLiveData;
    }

    public LiveData<List<User>> setAndGetUsersFollows() {
        Log.d(TAG, "setAndGetUsersFollows: ");
        usersFollowsLiveData = userRepository.getUserFollows();
        return usersFollowsLiveData;
    }

    public LiveData<User> setAndGetUserInfo() {
        Log.d(TAG, "setAndGetUserInfo: ");
        userInfoLiveData = userRepository.getUserInfo();
        return userInfoLiveData;
    }

    public LiveData<List<User>> setAndGetChatObject() {
        Log.d(TAG, "setAndGetChatObject: ");
        chatObjectLiveData = userRepository.getChatObject();
        return chatObjectLiveData;
    }
}

