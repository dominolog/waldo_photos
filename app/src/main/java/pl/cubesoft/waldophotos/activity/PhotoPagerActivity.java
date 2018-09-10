package pl.cubesoft.waldophotos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.fragment.PhotoPagerFragment;
import pl.cubesoft.waldophotos.fragment.PhotoPagerItemFragment;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.utils.TextUtils;


public class PhotoPagerActivity extends BaseActivity implements
        PhotoPagerFragment.OnPhotoPagerFragmentInteractionListener,
        PhotoPagerItemFragment.OnPhotoPagerItemFragmentInteractionListener{

    private static final String ARG_POSITION = "position";
    private static final String ARG_ALBUM_ID = "id";
    private ActionBar actionBar;
    private PhotoPagerFragment fragmentPhotoSetPager;

    public static int getPagerPositionFromBundle(Intent data) {
        return PhotoPagerFragment.getPagerPositionFromBundle(data);
    }



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        fragmentPhotoSetPager = (PhotoPagerFragment) fm.findFragmentById(R.id.container);
        if (savedInstanceState == null) {

            fm.beginTransaction().replace(R.id.container,
                    fragmentPhotoSetPager = PhotoPagerFragment.createInstance(
                            getIntent().getStringExtra(ARG_ALBUM_ID),
                            getIntent().getIntExtra(ARG_POSITION, 0)))
                    .commit();
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            Intent intent = new Intent();
            fragmentPhotoSetPager.setPagerPositionToBundle(intent);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        fragmentPhotoSetPager.setPagerPositionToBundle(intent);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    public static Intent createIntent(Context context, String albumId, Integer position) {

        return new Intent(context, PhotoPagerActivity.class)
                .putExtra(ARG_POSITION, position)
                .putExtra(ARG_ALBUM_ID, albumId);
    }




    @Override
    public void onPagerItemChanged(int pagePosition, int totalPages) {
        getSupportActionBar().setSubtitle(String.format("%d/%d", pagePosition+1, totalPages));
    }






    @Override
    public void onLoadAlbum(Album album) {
        getSupportActionBar().setTitle(album.getName());
        getSupportActionBar().setSubtitle(TextUtils.formatNumItems(album.getPhotos().size()));
    }



}
