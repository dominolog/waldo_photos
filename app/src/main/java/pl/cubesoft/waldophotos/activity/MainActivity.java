package pl.cubesoft.waldophotos.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.cubesoft.waldophotos.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startActivity(PhotoGridActivity.createIntent(this, "dTRydwXhGQgthi1r2cKFmg"));
    }
}
