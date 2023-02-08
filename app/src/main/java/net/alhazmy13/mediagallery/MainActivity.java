package net.alhazmy13.mediagallery;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.alhazmy13.mediagallery.library.activity.MediaGallery;
import net.alhazmy13.mediagallery.library.views.MediaGalleryView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaGalleryView.OnImageClicked {
    List<String> list;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = getFakeList();

        MediaGalleryView view = findViewById(R.id.gallery);
        view.setImages(list);
        view.setOnImageClickListener(this);
        view.setPlaceHolder(R.mipmap.ic_launcher);
        view.setOrientation(MediaGalleryView.HORIZONTAL);
//        view.setImageSize(500,MediaGalleryView.DEFAULT);
        view.notifyDataSetChanged();


    }

    private List<String> getFakeList() {
        List<String> imagesList = new ArrayList<>();
        imagesList.add("https://4kwallpapers.com/images/wallpapers/black-panther-8000x4500-10142.jpg");


        return imagesList;
    }

    @Override
    public void onImageClicked(int pos) {
        MediaGallery.Builder(this, list)
                .backgroundColor(R.color.white)
                .placeHolder(R.mipmap.ic_launcher)
                .selectedImagePosition(pos)
                .setShowDownload(true)
                .show();
    }
}
