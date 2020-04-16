package net.alhazmy13.mediagallery.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by alhazmy13 on 7/24/17.
 */

public final class Utility {
    public static GlideUrl getAuthorizedUrl(String url, String auth) {
        if (url.contains("amazonaws.com")) return new GlideUrl(url);
        LazyHeaders.Builder lazyHeaders = new LazyHeaders.Builder();
        if (!TextUtils.isEmpty(auth)) lazyHeaders.addHeader("Authorization", auth);
        return new GlideUrl(url,
                lazyHeaders.build());
    }

    public static boolean isValidURL(String urlString) {
        if (urlString != null && urlString.contains("gs://")) return true;
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isValidFilePath(String path) {
        try {
            Uri.fromFile(new File(path));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static ByteArrayOutputStream toByteArrayOutputStream(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream;
        } catch (Exception e) {
//            e.getMessage();
            return null;
        }
    }

    public static int getStatusbarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
