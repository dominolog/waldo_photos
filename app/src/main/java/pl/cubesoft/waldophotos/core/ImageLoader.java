package pl.cubesoft.waldophotos.core;

import android.net.Uri;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public interface ImageLoader {
    public void loadImage(Uri uri, ImageView photo, Object tag, boolean skipCache, ImageLoaderListener listener);

    void resumeLoad(String tag);

    void pauseLoad(String tag);

    void cancelLoad(String tag);

    void cancelRequest(ImageView imageView);

    public interface ImageLoaderListener {
        public void onImageLoadSuccess();
        public void onImageLoadError();
    }
}
