package org.nuist.blogapp.util;

import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.html.HtmlTag;
import io.noties.markwon.html.tag.SimpleTagHandler;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.RenderProps;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;

public class MarkwonHelper {

    private static Markwon instance;

    public static Markwon getInstance(Context context) {
        if (instance == null) {
            synchronized (MarkwonHelper.class) {
                if (instance == null) {
                    instance = Markwon.builder(context)

                            .usePlugin(HtmlPlugin.create(plugin -> {
                                plugin.addHandler(new SimpleTagHandler() {
                                    @NonNull
                                    @Override
                                    public Collection<String> supportedTags() {
                                        return Collections.singletonList("font");
                                    }

                                    @Nullable
                                    @Override
                                    public Object getSpans(@NonNull MarkwonConfiguration configuration,
                                                           @NonNull RenderProps renderProps,
                                                           @NonNull HtmlTag tag) {
                                        String color = tag.attributes().get("color");
                                        int parsedColor = Color.BLACK;
                                        if (color != null) {
                                            try {
                                                parsedColor = Color.parseColor(color);
                                            } catch (IllegalArgumentException ignored) {}
                                        }
                                        return new ForegroundColorSpan(parsedColor);
                                    }
                                });
                            }))




                            .usePlugin(ImagesPlugin.create())
                            .usePlugin(StrikethroughPlugin.create())
                            .usePlugin(TaskListPlugin.create(context))

                            .build();
                }
            }
        }

        return instance;
    }
}
