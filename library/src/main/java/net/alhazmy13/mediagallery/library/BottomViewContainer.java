package net.alhazmy13.mediagallery.library;

import android.app.Activity;
import android.view.View;

import androidx.annotation.LayoutRes;

import net.alhazmy13.mediagallery.library.activity.MediaGalleryActivity;

public abstract class BottomViewContainer {
    @LayoutRes
    public abstract int getViewId();

    public abstract void onValidateView(MediaGalleryActivity mediaGalleryActivity, View view, int position);

    public abstract void onActivityContextChanged(Activity activity);

    public int getImageRotation(int position) {
        return 0;
    }
}
