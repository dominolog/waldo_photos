package pl.cubesoft.waldophotos.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import pl.cubesoft.waldophotos.BuildConfig;

public class ImageLoaderPicasso implements ImageLoader {
	private final Context context;
	private final Picasso picasso;





	public ImageLoaderPicasso(Context context, OkHttpClient httpClient) {
		this.context = context;

		Picasso.Builder builder = new Picasso.Builder(this.context);
		builder.indicatorsEnabled(BuildConfig.DEBUG);
		builder.loggingEnabled(true);
		builder.downloader(new OkHttp3Downloader(httpClient));
		picasso = builder.build();

	}



	@Override
	public void loadImage(Uri uri, ImageView photo, Object tag) {
		picasso.load(uri)
				.fit()
				.centerInside()
				.tag(tag)
				.into(photo);
	}

	@Override
	public void resumeLoad(String tag) {
		picasso.resumeTag(tag);
	}

	@Override
	public void pauseLoad(String tag) {
		picasso.pauseTag(tag);
	}
}
