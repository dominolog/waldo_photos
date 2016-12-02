package pl.cubesoft.waldophotos.core;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public interface ImageLoader {
    public void loadImage(Uri uri, ImageView photo, Object tag);

    void resumeLoad(String tag);

    void pauseLoad(String tag);
}
