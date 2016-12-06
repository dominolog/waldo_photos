package pl.cubesoft.waldophotos.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.adapter.PhotoPagerAdapter;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.model.Model;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.model.dto.Photo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PhotoPagerFragment extends BaseFragment {

    private static final String IMAGE_LOAD_TAG = "galleryPagerView";
    private static final String ARG_POSITION = "position";
    private static final String ARG_ALBUM_ID = "albumId";
    private static final String BUNDLE_POSITION = "position";
    private static final String RESULT_PAGER_POSITION = "pagerPosition";
    private static final int NUM_ITEMS_TO_LOAD_PER_PAGE = 50;

    private OnPhotoPagerFragmentInteractionListener listener;


    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.emptyView)
    View emptyView;


    private Model model;
    private ImageLoader imageLoader;
    private PhotoPagerAdapter adapter;
    private Integer position;
    private String albumId;
    private List<Photo> items;
    private ViewPager.OnPageChangeListener pagerChangeListener;
    private Photo currentPhoto;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoPagerFragment() {
    }

    public void updateView() {
        refreshData(true);
    }

    public static int getPagerPositionFromBundle(Intent data) {
        return data.getIntExtra(RESULT_PAGER_POSITION, -1);
    }

    public void setPagerPositionToBundle(Intent data) {
        data.putExtra(RESULT_PAGER_POSITION, this.pager.getCurrentItem());
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")

    public static class GalleryItem {
        public long photoId;
    }


    public static PhotoPagerFragment createInstance(String albumId, Integer position) {
        PhotoPagerFragment fragment = new PhotoPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_ID, albumId);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            albumId = getArguments().getString(ARG_ALBUM_ID);
        }

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(BUNDLE_POSITION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_pager, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // Set the adapter
        pager.setAdapter(adapter = new PhotoPagerAdapter(getChildFragmentManager(), IMAGE_LOAD_TAG));
        pager.addOnPageChangeListener(pagerChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.onPagerItemChanged(position, adapter.getCount());
                onPhotoSelected(items.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void onPhotoSelected(Photo photo) {

    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(BUNDLE_POSITION, pager.getCurrentItem());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoPagerFragmentInteractionListener) {
            listener = (OnPhotoPagerFragmentInteractionListener) context;

            imageLoader = listener.getImageLoader();
            model = listener.getModel();

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData(false);
        imageLoader.resumeLoad(IMAGE_LOAD_TAG);


    }

    @Override
    public void onPause() {
        super.onPause();

        imageLoader.pauseLoad(IMAGE_LOAD_TAG);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        imageLoader.cancelLoad(IMAGE_LOAD_TAG);

    }


    private void refreshData(boolean force) {
        loadAlbum(force, albumId);

    }


    private void loadAlbum(boolean force, String albumId) {
        subscribe(model.loadAlbum(force, albumId, NUM_ITEMS_TO_LOAD_PER_PAGE, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(album -> {
                    setData(album);
                    listener.onLoadAlbum(album);
                }, throwable -> {
                    showSnackBar(android.R.id.content, R.string.error_while_loading_album);
                })
        );
    }



    void setData(Album album) {
        this.items = album.getPhotos();
        adapter.setData(items);
        pager.setCurrentItem(position, false);

        pager.post(() -> pagerChangeListener.onPageSelected(pager.getCurrentItem()));
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPhotoPagerFragmentInteractionListener {

        void onPagerItemChanged(int pagePosition, int totalPages);

        ImageLoader getImageLoader();

        Model getModel();


        void onLoadAlbum(Album album);
    }
}
