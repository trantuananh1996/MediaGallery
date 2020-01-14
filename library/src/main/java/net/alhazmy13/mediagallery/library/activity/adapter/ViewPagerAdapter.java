package net.alhazmy13.mediagallery.library.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.alhazmy13.mediagallery.library.BottomViewContainer;
import net.alhazmy13.mediagallery.library.R;
import net.alhazmy13.mediagallery.library.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


/**
 * The type View pager adapter.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mDataSet;
    private boolean isShowing = true;
    private RelativeLayout bottomView;
    private PhotoView imageView;
    private View topView;
    private BottomViewContainer bottomViewContainer;
    String auth;
    RecyclerView imagesHorizontalList;

    /**
     * Instantiates a new View pager adapter.
     *
     * @param activity             the activity
     * @param dataSet              the images
     * @param bottomView           the images horizontal list
     * @param bottomViewContainer
     * @param imagesHorizontalList
     * @param auth
     */
    public ViewPagerAdapter(Activity activity, ArrayList<String> dataSet, RelativeLayout bottomView, View topView, BottomViewContainer bottomViewContainer, RecyclerView imagesHorizontalList, String auth) {
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataSet = dataSet;
        this.bottomView = bottomView;
        this.topView = topView;
        this.bottomViewContainer = bottomViewContainer;
        this.auth = auth;
        this.imagesHorizontalList = imagesHorizontalList;
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        String o = mDataSet.get(position);
        boolean isImageValid;
        imageView = itemView.findViewById(R.id.iv);
        if (bottomViewContainer != null)
            imageView.setRotation(bottomViewContainer.getImageRotation(position));
        onTap();
        if (Utility.isValidURL(o)) {
            RequestBuilder<Drawable> option = Glide.with(activity)
                    .load(Utility.getAuthorizedUrl(String.valueOf(mDataSet.get(position)), auth));
            if (o.contains("gs://")) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(o);
                option = Glide.with(activity)
                        .load(storageRef);
            }
            try {
                option = option.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC));
            } catch (NoSuchMethodError ignored) {
            }
            option.into(imageView);
            isImageValid = true;
        } else if (Utility.isValidFilePath(o)) {
            RequestBuilder<Drawable> option = Glide.with(activity)
                    .load(new File(o));
            try {
                option = option.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC));
            } catch (NoSuchMethodError ignored) {
            }
            option
                    .into(imageView);
            isImageValid = true;
        } else {
            ByteArrayOutputStream stream = Utility.toByteArrayOutputStream(o);
            if (stream != null) {
                RequestBuilder<Bitmap> option = Glide.with(activity)
                        .asBitmap()
                        .load(stream.toByteArray());
                try {
                    option = option.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC));
                } catch (NoSuchMethodError ignored) {
                }
                option
                        .into(imageView);
                isImageValid = true;
            } else {
                throw new RuntimeException("Image at position: " + position + " it's not valid image");
            }

        }

        if (!isImageValid) {
            throw new RuntimeException("Value at position: " + position + " Should be as url string or bitmap object");
        }


        container.addView(itemView);

        return itemView;
    }

    private void onTap() {
        imageView.getAttacher().setOnPhotoTapListener((view, x, y) -> {
            if (isShowing) {
                isShowing = false;
                topView.animate().translationY(-topView.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                bottomView.animate().translationY(bottomView.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                imagesHorizontalList.animate().translationY(imagesHorizontalList.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
            } else {
                isShowing = true;
                topView.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                bottomView.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                imagesHorizontalList.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}
