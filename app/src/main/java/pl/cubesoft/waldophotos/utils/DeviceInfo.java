package pl.cubesoft.waldophotos.utils;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class DeviceInfo {
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }
}
