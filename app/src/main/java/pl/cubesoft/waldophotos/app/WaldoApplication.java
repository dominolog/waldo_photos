package pl.cubesoft.waldophotos.app;

import android.app.Application;


/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class WaldoApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
