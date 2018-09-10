package pl.cubesoft.waldophotos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.fragment.PhotoGridFragment;
import pl.cubesoft.waldophotos.model.Model;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.utils.TextUtils;

public class PhotoGridActivity extends BaseActivity implements PhotoGridFragment.OnPhotoGridFragmentInteractionListener {

    private static final String ARG_ALBUM_ID = "id";
    private String albumId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        albumId = getIntent().getStringExtra(ARG_ALBUM_ID);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    PhotoGridFragment.createInstance(
                            albumId
                    )).commit();
        }
    }

    public static Intent createIntent(Context context, String id) {

        return new Intent(context, PhotoGridActivity.class)
                .putExtra(ARG_ALBUM_ID, id);

    }

    @Override
    public ImageLoader getImageLoader() {
        return getWaldoApplication().provideImageLoader();
    }

    @Override
    public Model getModel() {
        return getWaldoApplication().provideModel();
    }

    @Override
    public void onLoadAlbum(Album album) {
        getSupportActionBar().setTitle(album.getName());
        getSupportActionBar().setSubtitle(TextUtils.formatNumItems(album.getPhotos().size()));
    }
}
