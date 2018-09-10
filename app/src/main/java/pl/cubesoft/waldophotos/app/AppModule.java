package pl.cubesoft.waldophotos.app;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.cubesoft.waldophotos.BuildConfig;
import pl.cubesoft.waldophotos.api.PhotosAPI;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.core.ImageLoaderPicasso;
import pl.cubesoft.waldophotos.viewmodel.AlbumRepository;
import pl.cubesoft.waldophotos.viewmodel.ViewModelFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }


    @Provides
    @Singleton
    public PhotosAPI providePhotosAPI(Retrofit retrofit) {
        return retrofit.create(PhotosAPI.class);
    }


    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.WEB_SERVICE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(Application application) {

        final OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .cache(new Cache(application.getExternalCacheDir(), Integer.MAX_VALUE))
                .followRedirects(true)
                .addNetworkInterceptor(chain -> {
                    final Request.Builder requestBuilder = chain.request().newBuilder();


                    Map<String, String> requestHeaders = buildHttpHeaderList();
                    if (requestHeaders != null) {

                        for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                            requestBuilder.removeHeader(entry.getKey());
                            requestBuilder.addHeader(entry.getKey(), entry.getValue());
                        }
                    }

                    final Request newRequest = requestBuilder
                            .build();
                    return chain.proceed(newRequest);
                });

        if (BuildConfig.LOG_HTTP_CONTENT) {
            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(logging);
        }


        return builder.build();
    }

    private Map<String, String> buildHttpHeaderList() {
        Map<String, String> result = new HashMap<>();

        // hardcoded auth token

        result.put("Cookie", BuildConfig.AUTH_COOKIE);
        result.put("User-Agent", BuildConfig.USER_AGENT);
        return result;
    }

    @Provides
    @Singleton
    public ImageLoader provideImageLoader(Application application, OkHttpClient okHttpClient) {
        return new ImageLoaderPicasso(application, okHttpClient);
    }



    @Provides
    @Singleton
    public ViewModelFactory provideViewModelFactory(AlbumRepository albumRepository) {
        return new ViewModelFactory(albumRepository);
    }

    @Provides
    @Singleton
    public AlbumRepository provideAlbumRepository(PhotosAPI photosAPI) {
        return new AlbumRepository(photosAPI);
    }
}
