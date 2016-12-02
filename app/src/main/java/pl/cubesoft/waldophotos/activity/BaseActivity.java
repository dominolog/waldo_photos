package pl.cubesoft.waldophotos.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pl.cubesoft.waldophotos.WaldoApplication;

/**
 * Created by CUBESOFT on 01.12.2016.
 */

public class BaseActivity extends AppCompatActivity {

    private boolean isImmersive;

    protected WaldoApplication getWaldoApplication() {
        return (WaldoApplication)getApplication();
    }

    protected void setImmersiveMode(boolean immersiveMode) {
        this.isImmersive = immersiveMode;

        if ( immersiveMode ) {
            enableImmersiveMode();
        } else {
            disableImmersiveMode();
        }
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus ) {
            if (isImmersive) {
                enableImmersiveMode();
            } else {
                disableImmersiveMode();
            }
        }
    }

    // This snippet hides the system bars.
    private void enableImmersiveMode() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void disableImmersiveMode() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
