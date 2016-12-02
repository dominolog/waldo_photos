package pl.cubesoft.waldophotos.fragment;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.adapter.PhotoGridAdapter;
import pl.cubesoft.waldophotos.model.Model;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.view.EmptyRecyclerView;
import pl.cubesoft.waldophotos.view.EndlessRecyclerViewScrollListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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





    private String albumId;
    private OnPhotoGridFragmentInteractionListener listener;
    private ImageLoader imageLoader;
    private Model model;
    private Album album;
    //private int currentPage = 0;


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
        container.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                //your padding...
                final int padding = 1;
                final int itemPosition = parent.getChildAdapterPosition(view);
                if (itemPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                final int itemCount = state.getItemCount();


                if (itemPosition == 0) {
                    outRect.set(padding, padding, 0, padding);
                }

                else if (itemCount > 0 && itemPosition == itemCount - 1) {
                    outRect.set(0, padding, padding, padding);
                }

                else {
                    outRect.set(padding, padding, padding, padding);
                }

            }
        });
        adapter = new PhotoGridAdapter(getContext(), model, imageLoader, IMAGE_LOAD_TAG, COLUMN_COUNT);
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

        container.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int page) {
                refreshData(false, NUM_ITEMS_TO_LOAD_PER_PAGE, adapter.getItemCount());

            }
        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true, NUM_ITEMS_TO_LOAD_PER_PAGE, 0);
        });

        adapter.setOnItemClickListener((view1, position, id) -> {
            onItemClick(view1, position, id);

        });


        adapter.setOnItemLongClickListener((view1, position, id) -> {
            return onItemLongClick(view1, position, id);
        });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }


        refreshData(false, NUM_ITEMS_TO_LOAD_PER_PAGE, adapter.getItemCount());
    }

    private void onItemClick(View view, int position, String id) {
        final int idx = container.getChildAdapterPosition(view);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumId = getArguments().getString(ARG_ALBUM_ID);

        setHasOptionsMenu(true);


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
        subscribe(model.loadAlbum(force, albumId, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(album -> {

                    adapter.setData(album);
                    this.album = album;
                    listener.onLoadAlbum(album);

                    getActivity().supportInvalidateOptionsMenu();
                }, throwable -> {
                    setRefreshing(false);
                    showSnackBar(android.R.id.content, R.string.error_while_loading_album);
                }, () -> {

                    setRefreshing(false);

                })
        );
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoGridFragmentInteractionListener) {
            listener = (OnPhotoGridFragmentInteractionListener) context;

            imageLoader = listener.getImageLoader();
            model = listener.getModel();



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





    public interface OnPhotoGridFragmentInteractionListener {
        ImageLoader getImageLoader();

        Model getModel();

        void onLoadAlbum(Album album);


    }

}