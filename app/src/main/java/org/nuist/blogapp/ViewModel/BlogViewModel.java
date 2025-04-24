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
}
