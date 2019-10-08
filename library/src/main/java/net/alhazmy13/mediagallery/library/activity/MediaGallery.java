package net.alhazmy13.mediagallery.library.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import net.alhazmy13.mediagallery.library.BottomViewContainer;
import net.alhazmy13.mediagallery.library.Constants;
import net.alhazmy13.mediagallery.library.MenuHandler;
import net.alhazmy13.mediagallery.library.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static net.alhazmy13.mediagallery.library.Constants.AUTHORIZATION;


public class MediaGallery {
    private String baseUrl;
    private String auth;

    public static String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Context mActivity;
    private ArrayList<String> mDataset;
    private int mSelectedImagePosition;
    @ColorRes
    private int mBackgroundColor = -1;
    @ColorRes
    private int buttonColor = -1;
    @DrawableRes
    private int mPlaceHolder;
    private BottomViewContainer bottomViewContainer;
    private int saveImageTitle;
    private boolean showDownload = true;
    private MenuHandler menuHandler;
    private boolean showHorizontalList = false;

    public static MediaGallery Builder(Context activity, ArrayList<String> imagesURLs) {
        return new MediaGallery(activity, imagesURLs);
    }


    public MediaGallery attachBottomView(BottomViewContainer bottomViewContainer) {
        this.bottomViewContainer = bottomViewContainer;
        return this;
    }


    private MediaGallery(Context context, ArrayList<String> imagesList) {
        this.mDataset = imagesList;
        this.mActivity = context;
    }

    public MediaGallery backgroundColor(@ColorRes int color) {
        this.mBackgroundColor = color;
        return this;
    }

    public MediaGallery placeHolder(@DrawableRes int placeholder) {
        this.mPlaceHolder = placeholder;
        return this;
    }

    public MediaGallery selectedImagePosition(int position) {
        this.mSelectedImagePosition = position;
        return this;
    }

    public MediaGallery setShowDownload(boolean showDownload) {
        this.showDownload = showDownload;
        return this;
    }

    public MediaGallery setShowHorizontalList(boolean showHorizontalList) {
        this.showHorizontalList = showHorizontalList;
        return this;
    }

    public MediaGallery setMenuHandler(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
        return this;
    }


    private String validateImageUrl(String url) {
        if (TextUtils.isEmpty(url)) return "";
        url = url.replace(" ", "%20");
        if (url.contains("http") || url.contains("gs://")) return url;
        if (url.startsWith("/")) url = url.replaceFirst("/", "");
        if (baseUrl.endsWith("/"))
            return baseUrl + url;
        else return baseUrl + "/" + url;
    }

    public void show() {
        Intent intent = new Intent(mActivity, MediaGalleryActivity.class);
        Bundle bundle = new Bundle();
        List<String> mData = new ArrayList<>(mDataset);
        mDataset.clear();
        for (String string : mData) {
            if (!Utility.isValidFilePath(string))
                mDataset.add(validateImageUrl(string));
            else mDataset.add(string);
        }

        bundle.putStringArrayList(Constants.IMAGES, mDataset);
        bundle.putString(AUTHORIZATION, auth);
        bundle.putInt(Constants.BACKGROUND_COLOR, mBackgroundColor);
        bundle.putInt(Constants.BUTTON_COLOR, buttonColor);
        bundle.putInt(Constants.PLACE_HOLDER, mPlaceHolder);
        bundle.putInt(Constants.SAVE_IMAGE_TITLE, saveImageTitle);
        bundle.putInt(Constants.SELECTED_IMAGE_POSITION, mSelectedImagePosition);
        bundle.putBoolean(Constants.SHOW_DOWNLOAD, showDownload);
        bundle.putBoolean(Constants.SHOW_HORIZONTAL_LIST, showHorizontalList);
        BaseActivity.bottomViewContainer = bottomViewContainer;
        BaseActivity.menuHandler = menuHandler;
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }


    public MediaGallery saveImageTitle(@StringRes int resId) {
        this.saveImageTitle = resId;
        return this;
    }

    public MediaGallery setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public MediaGallery buttonColor(int buttonColor) {
        this.buttonColor = buttonColor;
        return this;
    }

    public MediaGallery setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    public String getAuth() {
        return auth;
    }
}
