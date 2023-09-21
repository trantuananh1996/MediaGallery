package net.alhazmy13.mediagallery.library.activity.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.alhazmy13.mediagallery.library.R;
import net.alhazmy13.mediagallery.library.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * The type Horizontal list adapters.
 */
public class HorizontalListAdapters extends RecyclerView.Adapter<HorizontalListAdapters.ViewHolder> {
    private final int placeHolder;
    private final ArrayList<String> mDataset;
    private final Context mContext;
    private int mSelectedItem = -1;
    private final OnImgClick mClickListner;
    private final String auth;

    /**
     * Instantiates a new Horizontal list adapters.
     *
     * @param activity    the activity
     * @param images      the images
     * @param imgClick    the img click
     * @param placeHolder the place holder
     */
    public HorizontalListAdapters(Context activity, ArrayList<String> images, OnImgClick imgClick, int placeHolder, String auth) {
        this.mContext = activity;
        this.mDataset = images;
        this.mClickListner = imgClick;
        this.auth = auth;
        this.placeHolder = placeHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_horizontal, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String o = mDataset.get(holder.getAdapterPosition());
        boolean isValidImage;
        if (Utility.isValidURL(o)) {
            if (holder.image != null) Glide.with(mContext)
                    .load(Utility.getAuthorizedUrl(String.valueOf(o), auth))
                    .apply(new RequestOptions().placeholder(placeHolder == -1 ? R.drawable.media_gallery_placeholder : placeHolder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.image);
            if (o.contains("gs://")) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(o);
                Glide.with(mContext)
                        .load(storageRef)
                        .apply(new RequestOptions().placeholder(placeHolder == -1 ? R.drawable.media_gallery_placeholder : placeHolder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.image);
            }


            isValidImage = true;
        } else {

            ByteArrayOutputStream stream = Utility.toByteArrayOutputStream(o);
            if (stream != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(stream.toByteArray())
                        .apply(new RequestOptions()
                                .placeholder(placeHolder == -1 ? R.drawable.media_gallery_placeholder : placeHolder)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        )
                        .into(holder.image);
                isValidImage = true;
            } else {
                throw new RuntimeException("Image at position: " + position + " it's not valid image");

            }
        }
        if (!isValidImage) {
            throw new RuntimeException("Value at position: " + position + " Should be as url string or bitmap object");
        }
        ColorMatrix matrix = new ColorMatrix();
        if (mSelectedItem != holder.getAdapterPosition()) {
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.image.setColorFilter(filter);
            holder.image.setAlpha(0.5f);
        } else {
            matrix.setSaturation(1);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.image.setColorFilter(filter);
            holder.image.setAlpha(1f);
        }

        holder.image.setOnClickListener(view -> mClickListner.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Sets selected item.
     *
     * @param position the position
     */
    public void setSelectedItem(int position) {
        if (position >= mDataset.size()) return;
        mSelectedItem = position;
        notifyDataSetChanged();
    }

    /**
     * The type View holder.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Image.
         */
        public ImageView image;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv);
            if(image==null) image = itemView.findViewById(R.id.image);
        }
    }


    public interface OnImgClick {
        void onClick(int pos);
    }

}
