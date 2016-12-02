package pl.cubesoft.waldophotos.utils;

import java.util.List;

import pl.cubesoft.waldophotos.model.dto.Photo;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class TextUtils {
    public static String formatNumItems(int count) {
        String itemsString = Integer.toString(count);
        return itemsString + ((count != 1) ? " items" : " item");
    }

}
