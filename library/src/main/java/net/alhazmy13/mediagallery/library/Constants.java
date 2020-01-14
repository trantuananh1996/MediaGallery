package net.alhazmy13.mediagallery.library;

import android.Manifest;

public final class Constants {

    public static final String IMAGES = "IMAGES";
    public static final String SELECTED_IMG_POS = "SELECTED_IMG_POS";
    public static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public static final String PLACE_HOLDER = "PLACE_HOLDER";
    public static final String SELECTED_IMAGE_POSITION = "SELECTED_IMAGE_POSITION";
    public static final String BASE_URL = "BASE_URL";
    public static final String BOTTOM_VIEW_CONTAINEAR = "BOTTOM_VIEW";
    public static final String SAVE_IMAGE_TITLE = "SAVE_IMAGE_TITLE";
    public static final String SHOW_DOWNLOAD = "SHOW_DOWNLOAD";
    public static final String SHOW_HORIZONTAL_LIST = "SHOW_HORIZONTAL_LIST";
    public static final String BUTTON_COLOR = "BUTTON_COLOR";
    public static final String CLOSE_BUTTON_COLOR = "CLOSE_BUTTON_COLOR";
    public static final String NEXT_BUTTON_COLOR = "NEXT_BUTTON_COLOR";
    public static final String PREV_BUTTON_COLOR = "PREV_BUTTON_COLOR";
    public static final String DOWNLOAD_BUTTON_COLOR = "DOWNLOAD_BUTTON_COLOR";
    public static final String AUTHORIZATION = "auth";
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_EXTERNAL_STORAGE = 1;

}
