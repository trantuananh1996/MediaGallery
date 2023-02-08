package net.alhazmy13.mediagallery.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.alhazmy13.mediagallery.library.R;
import net.alhazmy13.mediagallery.library.views.adapter.GridImagesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alhazmy13 on 2/12/17.
 */
public class MediaGalleryView extends RecyclerView {
    private static final String TAG = "MediaGalleryView";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int DEFAULT = 6131;

    private final Context mContext;
    private GridImagesAdapter mAdapter;
    private ArrayList<String> mDataset;
    private Drawable mPlaceHolder;
    private OnImageClicked mOnImageClickListener;
    private int mSpanCount;
    private int mOrientation;
    private int mWidth;
    private int mHeight;

    /**
     * Instantiates a new Media gallery view.
     *
     * @param context the context
     */
    public MediaGalleryView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    /**
     * Instantiates a new Media gallery view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public MediaGalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MediaGalleryView, 0, 0);
        mSpanCount = a.getInteger(R.styleable.MediaGalleryView_span_count, 2);
        mPlaceHolder = a.getDrawable(R.styleable.MediaGalleryView_place_holder);
        mOrientation = a.getInt(R.styleable.MediaGalleryView_gallery_orientation, VERTICAL);
        mWidth = a.getDimensionPixelSize(R.styleable.MediaGalleryView_image_width, DEFAULT);
        mHeight = a.getDimensionPixelSize(R.styleable.MediaGalleryView_image_height, DEFAULT);
        if (mPlaceHolder == null) {
            mPlaceHolder = AppCompatResources.getDrawable(mContext, R.drawable.media_gallery_placeholder);
        }
        init();

    }

    /**
     * Init.
     */
    public void init() {
        mDataset = new ArrayList<>();
        //TODO:
        String auth = "";
        mAdapter = new GridImagesAdapter(mContext, mDataset, mPlaceHolder, auth);
        setOrientation(mOrientation);
        mAdapter.setImageSize(mWidth, mHeight);
        setAdapter(mAdapter);
    }

    /**
     * Sets images.
     *
     * @param itemList the item list
     */
    public void setImages(List<String> itemList) {
        this.mDataset.clear();
        this.mDataset.addAll(itemList);
    }

    /**
     * Notify data set changed.
     */
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            init();
        }
    }

    /**
     * Sets place holder.
     *
     * @param placeHolder the place holder
     */
    public void setPlaceHolder(int placeHolder) {
        this.mPlaceHolder = AppCompatResources.getDrawable(mContext, placeHolder);
        mAdapter.setImgPlaceHolder(mPlaceHolder);
    }

    /**
     * Sets on image click listener.
     *
     * @param onImageClickListener the on image click listener
     */
    public void setOnImageClickListener(OnImageClicked onImageClickListener) {
        this.mOnImageClickListener = onImageClickListener;
        mAdapter.setOnImageClickListener(mOnImageClickListener);
    }

    /**
     * span count in each row.
     *
     * @param spanCount the span count
     */
    public void setSpanCount(int spanCount) {
        this.mSpanCount = spanCount;
        setLayoutManager(new GridLayoutManager(mContext, mSpanCount));

    }

    /**
     * Sets orientation for image scrolling.
     *
     * @param orientation the orientation
     */
    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        if (orientation == HORIZONTAL) {
            setLayoutManager(new GridLayoutManager(mContext, mSpanCount, RecyclerView.HORIZONTAL, false));
        } else if (orientation == VERTICAL) {
            setLayoutManager(new GridLayoutManager(mContext, mSpanCount, RecyclerView.VERTICAL, false));

        }
    }


    public void setImageSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mAdapter.setImageSize(width, height);
    }


    /**
     * The interface On image clicked.
     */
    public interface OnImageClicked {
        /**
         * On image clicked.
         *
         * @param pos the pos
         */
        void onImageClicked(int pos);
    }


}
