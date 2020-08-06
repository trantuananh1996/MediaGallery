package net.alhazmy13.mediagallery.library.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.florent37.runtimepermission.PermissionResult;
import com.github.florent37.runtimepermission.RuntimePermission;
import com.pr.swalert.toast.ToastUtils;

import net.alhazmy13.mediagallery.library.R;
import net.alhazmy13.mediagallery.library.activity.adapter.CustomViewPager;
import net.alhazmy13.mediagallery.library.activity.adapter.HorizontalListAdapters;
import net.alhazmy13.mediagallery.library.activity.adapter.ViewPagerAdapter;

import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static net.alhazmy13.mediagallery.library.Utility.getStatusbarHeight;


/**
 * The type Media gallery activity.
 */
public class MediaGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener, HorizontalListAdapters.OnImgClick {
    private CustomViewPager mViewPager;
    private RelativeLayout bottomView;
    private RecyclerView imagesHorizontalList;
    private HorizontalListAdapters hAdapter;
    private RelativeLayout mMainLayout;
    private View topView;
    TextView tvCount;
    ImageView close, download, next, prev;

    private boolean showMenu = false;


    public long downloadID;
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toasty.success(MediaGalleryActivity.this, getString(R.string.download_success)).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onDownloadComplete);
        super.onDestroy();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void onCreateActivity() {
        if (bottomViewContainer != null) bottomViewContainer.onActivityContextChanged(this);
        // init layouts
        showMenu = menuHandler != null;
        initViews();

        setupViewPager();
    }

    private void setupViewPager() {

        mViewPager.setAdapter(new ViewPagerAdapter(this, dataSet, bottomView, topView, bottomViewContainer, imagesHorizontalList, auth));
        hAdapter = new HorizontalListAdapters(this, dataSet, this, placeHolder, auth);
        imagesHorizontalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesHorizontalList.setAdapter(hAdapter);
        hAdapter.notifyDataSetChanged();
        mViewPager.addOnPageChangeListener(this);
        hAdapter.setSelectedItem(selectedImagePosition);
        mViewPager.setCurrentItem(selectedImagePosition);
        displayMetaInfo(selectedImagePosition);
    }

    private void initViews() {
        imagesHorizontalList = findViewById(R.id.imagesHorizontalList);
        mViewPager = findViewById(R.id.pager);
        bottomView = findViewById(R.id.bottomView);
        mMainLayout = findViewById(R.id.mainLayout);
        tvCount = findViewById(R.id.tv_count);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        close = findViewById(R.id.iv_close);
        topView = findViewById(R.id.image_controller);
        download = findViewById(R.id.iv_download);
        close.setOnClickListener(v -> onBackPressed());

        download.setOnClickListener(v -> {
            if (showMenu) showMenu();
            else downloadImage();
        });
        handleMenuVisibility(showMenu);

        if (topView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) topView.getLayoutParams();
            marginLayoutParams.setMargins(0, getStatusbarHeight(this), 0, 0);
            topView.setLayoutParams(marginLayoutParams);
        }
        if (bottomViewContainer != null && bottomViewContainer.getViewId() != 0) {
            View view = View.inflate(this, bottomViewContainer.getViewId(), null);
            bottomView.addView(view);
        } else {
            bottomViewContainer = null;
        }

        if (backgroundColor != -1) {
            mMainLayout.setBackgroundColor(ContextCompat.getColor(this, backgroundColor));
        }
        if (allButtonColor != -1) {
            int color = ContextCompat.getColor(this, allButtonColor);
            ImageViewCompat.setImageTintList(next, ColorStateList.valueOf(color));
            ImageViewCompat.setImageTintList(prev, ColorStateList.valueOf(color));
//            ImageViewCompat.setImageTintList(download, ColorStateList.valueOf(color));
            ImageViewCompat.setImageTintList(close, ColorStateList.valueOf(color));
            tvCount.setTextColor(color);
        }
        next.setOnClickListener(view -> {
            try {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        prev.setOnClickListener(view -> {
            try {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        setEnableSwipe(false);


        imagesHorizontalList.setVisibility(showHorizontalList ? View.VISIBLE : View.GONE);
        download.setVisibility(showDownload ? View.VISIBLE : View.GONE);

    }

    public void handleMenuVisibility(boolean isMenuShow) {
        if (isMenuShow) {
            download.setImageResource(R.drawable.ic_menu);
        } else {
            download.setImageResource(R.drawable.ic_download);
        }
    }

//    private void handleMenu() {
//        if (menuHandler == null) {
//            download.setOnClickListener(view -> downloadImage());
//            return;
//        }
//        download.setImageResource(R.drawable.ic_menu);
//        download.setOnClickListener(v -> showMenu());
//    }

    void downloadImage() {
        ToastUtils.alertYesNo(MediaGalleryActivity.this, saveImageTitle, yesButtonConfirmed -> {
            if (yesButtonConfirmed) {
                askPermission()
                        .onAccepted(result -> new SaveImageHelper(MediaGalleryActivity.this).saveImage(baseUrl, dataSet.get(selectedImagePosition), auth))
                        .ask();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public RuntimePermission askPermission(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            permissions = new String[2];
            permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
            permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }
        return RuntimePermission.askPermission(this, permissions)
                .onDenied(this::onDeniedPermissions)
                .onForeverDenied(this::onForeverDeniedPermissions);
    }

    private void onDeniedPermissions(PermissionResult result) {
        StringBuilder denied = getPermissionsString(result, result.getDenied());
        ToastUtils.alertYesNo(this, String.format(getString(R.string.ask_perrmission), denied.toString()), yesButtonConfirmed -> {
            if (yesButtonConfirmed) {
                result.askAgain();
            }
        });
    }

    private void onForeverDeniedPermissions(PermissionResult result) {
        StringBuilder denied = getPermissionsString(result, result.getForeverDenied());
        ToastUtils.alertYesNo(this, String.format(getString(R.string.ask_perrmission), denied.toString()), yesButtonConfirmed -> {
            if (yesButtonConfirmed) {
                result.goToSettings();
            }
        });
    }

    @NonNull
    private StringBuilder getPermissionsString(PermissionResult result, List<String> foreverDenied) {
        StringBuilder denied = new StringBuilder();
        for (String permission : foreverDenied) {
            try {
                denied.append("- ").append(getPackageManager().getPermissionInfo(permission, 0).loadLabel(getPackageManager()));
                if (result.getDenied().indexOf(permission) != result.getDenied().size() - 1)
                    denied.append("\n");
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        return denied;
    }


    @Override
    public void showMenu() {
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, download, Gravity.END);
        popup.getMenu().addSubMenu(Menu.NONE, 1, Menu.NONE, R.string.download);
        popup.getMenuInflater().inflate(menuHandler.getMenuId(), popup.getMenu());
        menuHandler.onMenuCreated(popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1)
                downloadImage();
            else
                menuHandler.onMenuItemClick(item, selectedImagePosition);
            return true;
        });
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private void resetZoomPage(int position) {
        View view = mViewPager.getChildAt(position);
        if (view != null) {
            PhotoView imageViewPreview = view.findViewById(R.id.iv);
            if (imageViewPreview != null) imageViewPreview.getAttacher().setScale(1f);
        }
    }

    public void rotate(int position, String path) {
        dataSet.set(position, path);
        setupViewPager();
    }

    public void delete(int position) {
        dataSet.remove(position);
        if (selectedImagePosition >= dataSet.size()) selectedImagePosition = dataSet.size() - 1;
        setupViewPager();
    }

    @Override
    public void onPageSelected(int position) {
        selectedImagePosition = position;
        displayMetaInfo(position);
        if (position > 0)
            resetZoomPage(position - 1);
        if (position < dataSet.size() - 1)
            resetZoomPage(position + 1);

        imagesHorizontalList.smoothScrollToPosition(position);
        hAdapter.setSelectedItem(position);
    }

    private void displayMetaInfo(int position) {
        tvCount.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, dataSet.size()));
        if (dataSet.size() == 1) {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
        } else if (position == dataSet.size() - 1) {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.VISIBLE);
        } else if (position == 0) {
            prev.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        } else {
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        try {
            if (bottomViewContainer != null)
                bottomViewContainer.onValidateView(MediaGalleryActivity.this, bottomView, position);
        } catch (AbstractMethodError ignored) {
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(int pos) {
        mViewPager.setCurrentItem(pos, true);
    }


}
