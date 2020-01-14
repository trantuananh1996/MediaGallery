package net.alhazmy13.mediagallery.library.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.URLUtil;

import com.pr.swalert.toast.ToastUtils;

import net.alhazmy13.mediagallery.library.R;

import static net.alhazmy13.mediagallery.library.activity.BaseActivity.isStoragePermissionGranted;


/**
 * Created by Tran Anh
 * on 4/11/2017.
 */

public class SaveImageHelper {
    private Activity activity;

    public SaveImageHelper(Activity activity) {
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isStoragePermissionGranted(activity);
        }
    }


    public static boolean checkIfDownloadManagerEnabled(Activity activity) {
        if (activity == null) return false;
        int state = activity.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            ToastUtils.alertYesNo(activity, R.string.w_download_manager_disabled,
                    yesButtonConfirmed -> {
                        if (yesButtonConfirmed) {
                            // Cannot download using download manager
                            String packageName = "com.android.providers.downloads";
                            try {
                                //Open the specific App Info page:
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + packageName));
                                activity.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                //Open the generic Apps page:
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                activity.startActivity(intent);
                            }
                        }
                    });
            return false;
        } else return true;
    }


    public void saveImage(String baseUrl, String url, String auth) {
        if (checkIfDownloadManagerEnabled(activity)) {
            url = MediaGallery.validateImageUrl(baseUrl, url);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)).addRequestHeader("Authorization", auth);
            request.setDescription("");
            String fileName = URLUtil.guessFileName(url, null, null);
            request.setTitle(fileName);
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            try {
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName);
            } catch (Throwable e) {
                request.setDestinationInExternalPublicDir("/Pictures", fileName);
            }
            DownloadManager manager = (DownloadManager) activity.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
            }
        }
    }


}
