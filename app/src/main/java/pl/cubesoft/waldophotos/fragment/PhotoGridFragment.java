package pl.cubesoft.waldophotos.fragment;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.activity.PhotoPagerActivity;
import pl.cubesoft.waldophotos.adapter.PhotoGridAdapter;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.view.EmptyRecyclerView;
import pl.cubesoft.waldophotos.view.EndlessRecyclerViewScrollListener;
import pl.cubesoft.waldophotos.viewmodel.AlbumRepository;
import pl.cubesoft.waldophotos.viewmodel.AlbumViewModel;
import pl.cubesoft.waldophotos.viewmodel.ViewModelFactory;


public class PhotoGridFragment extends BaseFragment {
    private static final String ARG_ALBUM_ID = "id";
    private static final int NUM_ITEMS_TO_LOAD_PER_PAGE = 50;
    private final String IMAGE_LOAD_TAG = "photoSetTag" + this.getTag();
    private static final int COLUMN_COUNT = 3;

    private PhotoGridAdapter adapter;

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    @BindView(R.id.container)
    EmptyRecyclerView container;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty)
    View emptyView;

    @Inject
    ImageLoader imageLoader;

    @Inject
    ViewModelFactory viewModelFactory;




    private String albumId;
    private OnPhotoGridFragmentInteractionListener listener;


    private Album album;
    private EndlessRecyclerViewScrollListener scrollListener;
    private AlbumViewModel model;
    //private int currentPage = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().getAppComponent().inject(this);




        albumId = getArguments().getString(ARG_ALBUM_ID);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photogrid, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        container.setEmptyView(emptyView);

        //final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), COLUMN_COUNT);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);


        container.setLayoutManager(layoutManager);

        adapter = new PhotoGridAdapter(getContext(), imageLoader, IMAGE_LOAD_TAG, COLUMN_COUNT);
        container.setAdapter(adapter);

        container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    imageLoader.resumeLoad(IMAGE_LOAD_TAG);
                } else {
                    imageLoader.pauseLoad(IMAGE_LOAD_TAG);
                }
            }


        });

        container.addOnScrollListener(scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                refreshData(false, NUM_ITEMS_TO_LOAD_PER_PAGE, adapter.getItemCount());
            }



        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true, NUM_ITEMS_TO_LOAD_PER_PAGE, 0);
        });

        adapter.setOnItemClickListener((view1, position, id) -> {
            onItemClick(view1, position, id);

        });


        adapter.setOnItemLongClickListener((view1, position, id) -> onItemLongClick(view1, position, id));




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }


        model = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel.class);
        model.getData(albumId).observe(this, this::setData);
        model.getState(albumId).observe(this, this::setState);

        refreshData(false, NUM_ITEMS_TO_LOAD_PER_PAGE, adapter.getItemCount());
    }



    private void onItemClick(View view, int position, String id) {
        final int idx = container.getChildAdapterPosition(view);
        ContextCompat.startActivity(getContext(), PhotoPagerActivity.createIntent(getContext(), albumId, position), null);
    }

    private boolean onItemLongClick(View view, int position, String id) {
        final int idx = container.getChildAdapterPosition(view);

        return true;
    }


    public static PhotoGridFragment createInstance(String albumId) {
        final PhotoGridFragment fragment = new PhotoGridFragment();


        final Bundle args = new Bundle();
        args.putString(ARG_ALBUM_ID, albumId);

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }


    @Override
    public void onPause() {
        super.onPause();

        // pause all image loading tasks
        imageLoader.pauseLoad(IMAGE_LOAD_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageLoader.resumeLoad(IMAGE_LOAD_TAG);
    }

    private void refreshData(boolean force, int limit, int offset) {

        loadAlbum(force, albumId, limit, offset);

    }

    private void loadAlbum(boolean force, String albumId, int limit, int offset) {
        model.loadData(albumId, limit, offset);
    }

    /*

    private void loadAlbum(boolean force, String albumId, int limit, int offset) {
        subscribe(model.loadAlbum(force, albumId, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(album -> {

                    setData(album);

                }, throwable -> {
                    setRefreshing(false);
                    showSnackBar(android.R.id.content, R.string.error_while_loading_album);
                }, () -> {

                    setRefreshing(false);
                    scrollListener.setIsLoading(false);
                })
        );
    }

*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoGridFragmentInteractionListener) {
            listener = (OnPhotoGridFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotoSetFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }








    private void setRefreshing(final boolean refreshing) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(refreshing));
    }

    public void setData(Album album) {
        adapter.setData(album);
        this.album = album;
        scrollListener.setNumItems(album.getPhotos().size());
        scrollListener.setIsLoading(false);
        listener.onLoadAlbum(album);

        getActivity().supportInvalidateOptionsMenu();
    }

    private void setState(AlbumRepository.State state) {
        switch(state) {
            case LOADING:
                setRefreshing(true);
                break;

            case LOADED:
                setRefreshing(false);
                break;

            case LOAD_ERROR:
                setRefreshing(false);
                showSnackBar(R.id.coordinator, R.string.error_loading_album);
                break;
        }
    }

    public interface OnPhotoGridFragmentInteractionListener {
        void onLoadAlbum(Album album);
    }

}