package pl.cubesoft.waldophotos.activity;

import android.support.v7.app.AppCompatActivity;

import pl.cubesoft.waldophotos.app.WaldoApplication;

/**
 * Created by CUBESOFT on 01.12.2016.
 */

public class BaseActivity extends AppCompatActivity {
    protected WaldoApplication getWaldoApplication() {
        return (WaldoApplication)getApplication();
    }
}
