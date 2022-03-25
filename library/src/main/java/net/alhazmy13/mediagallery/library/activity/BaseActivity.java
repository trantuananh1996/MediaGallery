package net.alhazmy13.mediagallery.library.activity;

import static net.alhazmy13.mediagallery.library.Constants.AUTHORIZATION;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.jaeger.library.StatusBarUtil;

import net.alhazmy13.mediagallery.library.BottomViewContainer;
import net.alhazmy13.mediagallery.library.Constants;
import net.alhazmy13.mediagallery.library.MenuHandler;

import java.util.ArrayList;


abstract class BaseActivity extends SwipeBackActivity {
    protected ArrayList<String> dataSet;
    @ColorRes
    protected int backgroundColor = -1;
    @ColorRes
    protected int allButtonColor = -1;
    @ColorRes
    private int closeButtonColor = -1;
    @ColorRes
    private int nextButtonColor = -1;
    @ColorRes
    private int prevButtonColor = -1;
    @ColorRes
    private int downloadButtonColor = -1;
    @DrawableRes
    protected int placeHolder;
    protected int selectedImagePosition;
    public static BottomViewContainer bottomViewContainer;
    protected int saveImageTitle;
    public static MenuHandler menuHandler;
    protected String auth;
    protected boolean showDownload = true;
    protected boolean showHorizontalList = false;
    protected String baseUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayoutId());
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        setDragEdge(SwipeBackLayout.DragEdge.BOTTOM);
        initBase();
        onCreateActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
    }

    protected void showMenu() {

    }

    private void initBase() {
        initBaseValues();
    }

    private void initBaseValues() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) return;
        Bundle bundle = intent.getExtras();
        dataSet = bundle.getStringArrayList(Constants.IMAGES);
        auth = bundle.getString(AUTHORIZATION);
        backgroundColor = bundle.getInt(Constants.BACKGROUND_COLOR, -1);
        allButtonColor = bundle.getInt(Constants.BUTTON_COLOR, -1);
        closeButtonColor = bundle.getInt(Constants.CLOSE_BUTTON_COLOR, -1);
        nextButtonColor = bundle.getInt(Constants.NEXT_BUTTON_COLOR, -1);
        prevButtonColor = bundle.getInt(Constants.PREV_BUTTON_COLOR, -1);
        downloadButtonColor = bundle.getInt(Constants.DOWNLOAD_BUTTON_COLOR, -1);
        placeHolder = bundle.getInt(Constants.PLACE_HOLDER, -1);
        selectedImagePosition = bundle.getInt(Constants.SELECTED_IMAGE_POSITION, 0);
        saveImageTitle = bundle.getInt(Constants.SAVE_IMAGE_TITLE, 0);
        showDownload = bundle.getBoolean(Constants.SHOW_DOWNLOAD, true);
        showHorizontalList = bundle.getBoolean(Constants.SHOW_HORIZONTAL_LIST, false);
        baseUrl = bundle.getString(Constants.BASE_URL, "");
    }


    protected abstract int getResourceLayoutId();

    protected abstract void onCreateActivity();

    public static boolean isStoragePermissionGranted(@Nullable Activity activity) {
        if (activity == null) return false;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED || permission1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    Constants.PERMISSIONS_STORAGE,
                    Constants.REQUEST_EXTERNAL_STORAGE
            );
            return false;
        } else return true;
    }

}
