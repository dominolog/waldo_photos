package pl.cubesoft.waldophotos.app;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import pl.cubesoft.waldophotos.fragment.PhotoGridFragment;
import pl.cubesoft.waldophotos.fragment.PhotoPagerFragment;
import pl.cubesoft.waldophotos.fragment.PhotoPagerItemFragment;


/**
 * Created by CUBESOFT on 16.01.2017.
 */


@Singleton
@Component(modules={AppModule.class})
public interface AppComponent extends AndroidInjector<WaldoApplication> {
    void inject(PhotoGridFragment photoGridFragment);

    void inject(PhotoPagerFragment photoPagerFragment);

    void inject(PhotoPagerItemFragment photoPagerItemFragment);


    // to update the fields in your activities
}
