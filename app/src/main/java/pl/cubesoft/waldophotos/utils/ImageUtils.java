package pl.cubesoft.waldophotos.utils;

import java.util.List;

import pl.cubesoft.waldophotos.model.dto.Url;

/**
 * Created by CUBESOFT on 06.12.2016.
 */

public class ImageUtils {

    public static Url getBestFitImageUrl(int viewWidth, List<Url> urls) {
        Url result = null;
        for (Url urlToCheck : urls) {
            if ( urlToCheck.getWidth() >= viewWidth ) {
                result = urlToCheck;
                break;
            }
        }

        // fallback
        if (result == null) {
            result = urls.get( urls.size() - 1);
        }
        return result;
    }

}
