package plugin.google.maps;

import android.graphics.Bitmap;

/**
 * Created by alex on 23/06/2017.
 */

public class LocalImageCache {
    private BitmapCache mIconCache;

    public LocalImageCache() {
        if (mIconCache != null) {
            return;
        }

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = maxMemory / 8;

        mIconCache = new BitmapCache(cacheSize);
    }

    public void addBitmapToMemoryCache(String key, Bitmap image) {
        if (getBitmapFromMemCache(key) == null) {
            mIconCache.put(key, image.copy(image.getConfig(), true));
        }
    }

    public void removeBitmapFromMemCache(String key) {
        Bitmap image = mIconCache.remove(key);
        if (image == null || image.isRecycled()) {
            return;
        }

        image.recycle();
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Bitmap image = mIconCache.get(key);
        if (image == null || image.isRecycled()) {
            return null;
        }

        return image.copy(image.getConfig(), true);
    }

    public void clear() {
        mIconCache.evictAll();
    }
}
