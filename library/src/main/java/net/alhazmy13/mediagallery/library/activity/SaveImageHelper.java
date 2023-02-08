package net.alhazmy13.mediagallery.library.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;


/**
 * Created by Tran Anh
 * on 4/11/2017.
 */

public class SaveImageHelper {
    private final MediaGalleryActivity activity;

    public SaveImageHelper(MediaGalleryActivity activity) {
        this.activity = activity;
//        isStoragePermissionGranted(activity);
    }

    public void saveImage(String baseUrl, String url, String auth) {
        url = MediaGallery.validateImageUrl(baseUrl, url);
        String fileName = URLUtil.guessFileName(url, null, null);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)).addRequestHeader("Authorization", auth);
        request.setDescription("");
        request.setTitle(fileName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);

        DownloadManager manager = (DownloadManager) activity.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            activity.mapDownload.put(manager.enqueue(request), Environment.DIRECTORY_PICTURES + fileName);
        }
    }
}

