package com.skala.runloop_app.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * @author Skala
 */
public class MemCache {
    private static final String TAG = MemCache.class.getSimpleName();

    private static final LruCache<String, Bitmap> mCache;

    static {
        final int maxSize = (int) ((Runtime.getRuntime().maxMemory() / 1024) / 8);
        mCache = new LruCache<String, Bitmap>(maxSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value == null) {
                    return 0;
                }

                int bytes;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    bytes = value.getAllocationByteCount() / 1024;
                } else {
                    bytes = value.getByteCount() / 1024;
                }

                return bytes;
            }
        };
    }

    public static boolean put(String key, Bitmap value) {
        Log.i(TAG, "put: " + key + " " + value);

        if (key == null || value == null) {
            return false;
        }

        synchronized (mCache) {
            if (mCache.get(key) == null) {
                mCache.put(key, value);

                Log.i(TAG, "size: " + size());

                return true;
            }
        }

        return false;
    }

    public static Bitmap get(String key) {
        if (key == null) {
            return null;
        }

        synchronized (mCache) {
            return mCache.get(key);
        }
    }

    public static int size() {
        synchronized (mCache) {
            return mCache.size();
        }
    }
}

