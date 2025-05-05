package org.nuist.blogapp.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.repository.BlogRepository;

import java.util.List;

public class BlogViewModel extends AndroidViewModel {
    private static final String TAG = "BlogViewModel";
    private BlogRepository blogRepository;
    private MutableLiveData<List<Blog>> blogsRecommended;
    private MutableLiveData<String> blog_content;
    private MutableLiveData<List<Blog>> blogsSearched;
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
}
