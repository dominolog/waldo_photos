package pl.cubesoft.waldophotos;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.cubesoft.waldophotos.api.PhotosAPI;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.core.ImageLoaderPicasso;
import pl.cubesoft.waldophotos.model.Model;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class WaldoApplication extends Application {

    private static  final String HEADER_CACHE_MAX_AGE_VAL = "max-age=2629000";
    private static  final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final boolean LOG_HTTP_CONTENT = BuildConfig.DEBUG;
    private static final String HEADER_COOKIE = "Cookie";
    private Retrofit retrofit;
    private Model model;
    private ImageLoader imageLoader;
    private OkHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private OkHttpClient provideHttpClient() {

        if ( httpClient == null ) {
            final OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .cache(new Cache(getExternalCacheDir(), Integer.MAX_VALUE))
                    .followRedirects(true)
                    .addNetworkInterceptor(chain -> {
                        final Builder requestBuilder = chain.request().newBuilder();


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
                    })
                    .addInterceptor(chain -> {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder().header(HEADER_CACHE_CONTROL, HEADER_CACHE_MAX_AGE_VAL).build();
                    });

            if (LOG_HTTP_CONTENT) {
                final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addNetworkInterceptor(logging);
            }



            httpClient = builder.build();
        }
        return httpClient;
    }

    private Map<String, String> buildHttpHeaderList() {
        Map<String, String>  result = new HashMap<>();

        // hardcoded auth token
        result.put(HEADER_COOKIE, BuildConfig.AUTH_COOKIE);
        result.put(HEADER_USER_AGENT, BuildConfig.USER_AGENT);
        return result;
    }

    private Retrofit provideRetrofit() {
        if ( retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.WEB_SERVICE_URL)
                    .client(provideHttpClient())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        }
        return retrofit;
    }

    public PhotosAPI providePhotosAPI() {
        PhotosAPI api = provideRetrofit().create(PhotosAPI.class);
        return api;
    }

    public Model provideModel() {
        if (model == null) {
            model = new Model(providePhotosAPI());
        }
        return model;
    }

    public ImageLoader provideImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoaderPicasso(getApplicationContext(), provideHttpClient());
        }
        return imageLoader;
    }
}
