package pl.cubesoft.waldophotos.core;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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
	public void loadImage(Uri uri, ImageView photo, Object tag, boolean skipCache, ImageLoaderListener listener) {
		RequestCreator builder = picasso.load(uri)
				.fit()
				.centerInside()
				.tag(tag);
		if (skipCache) {
			//builder.ski();
		}

		builder.into(photo, new Callback() {
			@Override
			public void onSuccess() {
				if (listener != null) {
					listener.onImageLoadSuccess();
				}
			}

			@Override
			public void onError(Exception e) {
				if (listener != null) {
					listener.onImageLoadError();
				}
			}


		});
	}

	@Override
	public void resumeLoad(String tag) {
		picasso.resumeTag(tag);
	}

	@Override
	public void pauseLoad(String tag) {
		picasso.pauseTag(tag);
	}

	@Override
	public void cancelLoad(String tag) {
		picasso.cancelTag(tag);
	}

	@Override
	public void cancelRequest(ImageView imageView) {
		picasso.cancelRequest(imageView);
	}
}
