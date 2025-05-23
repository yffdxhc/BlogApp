package org.nuist.blogapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.repository.BlogRepository;

import java.io.File;
import java.util.List;

public class BlogViewModel extends AndroidViewModel {
    private static final String TAG = "BlogViewModel";
    private BlogRepository blogRepository;
    private MutableLiveData<List<Blog>> blogsRecommended;
    private MutableLiveData<String> blog_content;
    private MutableLiveData<List<Blog>> blogsSearched;
    private MutableLiveData<Boolean> blogReleaseResult;
    private MutableLiveData<List<Blog>> blogsByUserNumber;
    private MutableLiveData<List<Blog>> hotBlogs;
    private MutableLiveData<Blog> blogInfoByBlogId;
    private MutableLiveData<Boolean> isBlogLike;
    private MutableLiveData<Boolean> isBlogMarked;
    private MutableLiveData<Boolean> likeButtonResult;
    public BlogViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "BlogViewModel: ");
        blogRepository = new BlogRepository(application);
        blogsRecommended = new MutableLiveData<>();
    }

    public MutableLiveData<List<Blog>> setAndGetBlogsRecommended() {
        Log.d(TAG, "getBlogsRecommended: ");
        blogsRecommended = blogRepository.getBlogsRecommended();
        return blogsRecommended;
    }

    public MutableLiveData<String> setAndGetBlogContent(String blog_id) {
        Log.d(TAG, "getBlogDocument: ");
        blog_content = blogRepository.getBlogDocument(blog_id);
        return blog_content;
    }

    public MutableLiveData<List<Blog>> setAndGetBlogsSearched(String query) {
        Log.d(TAG, "getBlogsSearched: ");
        blogsSearched = blogRepository.getBlogsSearched(query);
        return blogsSearched;
    }
    public MutableLiveData<Boolean> setAndGetBlogReleaseResult(String blog_title,
                                                              String content,
                                                              String blog_summary,
                                                              Integer type_id,
                                                              File coverImage) {
        Log.d(TAG, "blogRelease: "+ blog_title+ " "+content+" "+blog_summary+" "+type_id+" "+coverImage);
        blogReleaseResult = blogRepository.blogRelease(blog_title, content, blog_summary, type_id, coverImage);
        return blogReleaseResult;
    }
    public MutableLiveData<List<Blog>> setAndGetBlogsByUserNumber(String userNumber) {
        Log.d(TAG, "getBlogsByUserNumber: ");
        blogsByUserNumber = blogRepository.getBlogsByUserNumber(userNumber);
        return blogsByUserNumber;
    }

    public MutableLiveData<List<Blog>> setAndGetHotBlogs() {
        Log.d(TAG, "getHotBlogs: ");
        hotBlogs = blogRepository.getHotBlogs();
        return hotBlogs;
    }
    public MutableLiveData<Blog> setAndGetBlogInfoByBlogId(String blogId) {
        Log.d(TAG, "getBlogInfoByBlogId: ");
        blogInfoByBlogId = blogRepository.getBlogInfoByBlogId(blogId);
        return blogInfoByBlogId;
    }

    public MutableLiveData<Boolean> setAndGetIsBlogLike(String blogId) {
        Log.d(TAG, "isBlogLike: ");
        isBlogLike = blogRepository.isBlogLike(blogId);
        return isBlogLike;
    }
    public MutableLiveData<Boolean> setAndGetIsBlogMarked(String blogId) {
        Log.d(TAG, "isBlogMarked: ");
        isBlogMarked = blogRepository.isBlogMarked(blogId);
        return isBlogMarked;
    }
    public MutableLiveData<Boolean> setAndGetLikeButtonResult(String blogId) {
        Log.d(TAG, "likeButton: ");
        likeButtonResult = blogRepository.likeButton(blogId);
        return likeButtonResult;
    }
}
