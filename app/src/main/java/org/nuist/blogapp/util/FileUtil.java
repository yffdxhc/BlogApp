package org.nuist.blogapp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 与文件系统相关的工具类
 */
public class FileUtil {
    private static final String TAG="FileUtil";

    public static String getPathFromSAFUri(Uri uri, Context context) {
        // 如果传入的 URI 为空，直接返回 null
        if (uri == null) return null;

        // 检查 URI 是否为文档 URI
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 处理外部存储文档 URI
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                // 如果是主存储分区（primary），拼接路径并返回
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // 处理下载文档 URI
            else if (isDownloadsDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                if (docId.startsWith("raw:")) {
                    return docId.substring(4); // 去掉 "raw:" 前缀
                }
                // 使用 MediaStore.Downloads 处理其他类型的下载文档
                return getDataColumn(context, uri, null, null);
            }
            // 处理媒体文档 URI（图片、视频、音频）
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                // 根据类型设置不同的内容 URI
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                // 获取文件路径并返回
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // 处理内容 URI
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 获取文件路径并返回
            return getDataColumn(context, uri, null, null);
        }
        // 处理文件 URI
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 直接返回 URI 的路径部分
            return uri.getPath();
        }
        // 如果以上条件都不满足，返回 null
        return null;
    }

    /**
     * 从内容 URI 获取文件路径
     *
     * @param context       应用上下文
     * @param uri           内容 URI
     * @param selection     查询条件
     * @param selectionArgs 查询条件参数
     * @return 文件路径
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            // 查询内容 URI 对应的数据
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                // 获取列索引并返回文件路径
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            // 关闭游标
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * 检查 URI 是否为外部存储文档 URI
     *
     * @param uri URI
     * @return 如果是外部存储文档 URI，返回 true；否则返回 false
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 检查 URI 是否为下载文档 URI
     *
     * @param uri URI
     * @return 如果是下载文档 URI，返回 true；否则返回 false
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 检查 URI 是否为媒体文档 URI（图片、视频、音频）
     *
     * @param uri URI
     * @return 如果是媒体文档 URI，返回 true；否则返回 false
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
