package net.alhazmy13.mediagallery.library.views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.alhazmy13.mediagallery.library.R;
import net.alhazmy13.mediagallery.library.Utility;
import net.alhazmy13.mediagallery.library.views.MediaGalleryView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class GridImagesAdapter extends RecyclerView.Adapter<GridImagesAdapter.ViewHolder> {
    private static final String TAG = "GridImagesAdapter";
    private ArrayList<String> mDataset;
    private Context mContext;
    private Drawable imgPlaceHolderResId;
    private MediaGalleryView.OnImageClicked mClickListener;
    private int mHeight;
    private int mWidth;
    String auth;

    public GridImagesAdapter(Context activity, ArrayList<String> imageURLs, Drawable imgPlaceHolderResId, String auth) {
        this.mDataset = imageURLs;
        this.mContext = activity;
        this.imgPlaceHolderResId = imgPlaceHolderResId;
        this.auth = auth;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        boolean isImageValid;

        holder.itemView.setOnClickListener(view -> {
            if (mClickListener == null) return;
            mClickListener.onImageClicked(holder.getAdapterPosition());
        });

        ViewGroup.LayoutParams params = holder.image.getLayoutParams();

        if (mHeight != -1 && mHeight != MediaGalleryView.DEFAULT)
            params.height = mHeight;

        if (mWidth != -1 && mWidth != MediaGalleryView.DEFAULT)
            params.width = mWidth;

        holder.image.setLayoutParams(params);

        String o = mDataset.get(holder.getAdapterPosition());
        if (Utility.isValidURL(o)) {
            Glide.with(mContext)
                    .load(Utility.getAuthorizedUrl(o, auth))
                    .apply(new RequestOptions().placeholder(imgPlaceHolderResId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.image);
            isImageValid = true;
        } else {
            ByteArrayOutputStream stream = Utility.toByteArrayOutputStream(o);
            if (stream != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(stream.toByteArray())
                        .apply(new RequestOptions().placeholder(imgPlaceHolderResId).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.image);
                isImageValid = true;

            } else {
                throw new RuntimeException("Image at position: " + position + " it's not valid image");
            }

        }
        if (!isImageValid) {
            throw new RuntimeException("Value at position: " + position + " Should be as url string or bitmap object");
        }

    }


    public void setImgPlaceHolder(Drawable imgPlaceHolderResId) {
        this.imgPlaceHolderResId = imgPlaceHolderResId;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnImageClickListener(MediaGalleryView.OnImageClicked onImageClickListener) {
        this.mClickListener = onImageClickListener;
    }

    public void setImageSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }


}
