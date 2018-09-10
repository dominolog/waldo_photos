package pl.cubesoft.waldophotos.fragment;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.model.dto.Photo;
import pl.cubesoft.waldophotos.model.dto.Url;
import pl.cubesoft.waldophotos.model.parcelable.ParcelablePhoto;
import pl.cubesoft.waldophotos.utils.DeviceInfo;
import pl.cubesoft.waldophotos.utils.ImageUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoPagerItemFragment extends BaseFragment {

    private static final String ARG_IMAGE_LOAD_TAG = "imageLoadTag";
    private static final String ARG_PHOTO = "photo";
    private static final int REQUEST_DOWNLOAD_PHOTO = 100;
    private static final int REQUEST_DELETE_PHOTO = 101;

    @BindView(R.id.frame)
    View frame;


    @BindView(R.id.photo)
    PhotoView photo;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    ImageLoader imageLoader;

    private Object imageLoadTag;
    private PhotoViewAttacher photoAttacher;
    private Photo data;
    private OnPhotoPagerItemFragmentInteractionListener listener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoPagerItemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().getAppComponent().inject(this);

        if (getArguments() != null) {
            ParcelablePhoto parcelablePhoto = getArguments().getParcelable(ARG_PHOTO);
            data = parcelablePhoto.getPhoto();
            imageLoadTag = getArguments().getString(ARG_IMAGE_LOAD_TAG);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_pager_item, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        photoAttacher = new PhotoViewAttacher(photo);


        setData(data);

    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoPagerItemFragmentInteractionListener) {
            listener = (OnPhotoPagerItemFragmentInteractionListener) context;


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



    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        imageLoader.cancelRequest(photo);
    }




    @SuppressWarnings("unused")
    public static Fragment createInstance(Photo photo, String imageLoadTag) {
        PhotoPagerItemFragment fragment = new PhotoPagerItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, new ParcelablePhoto(photo));
        args.putString(ARG_IMAGE_LOAD_TAG, imageLoadTag);

        fragment.setArguments(args);
        return fragment;
    }


    public void setData(Photo data) {
        this.data = data;
        getActivity().supportInvalidateOptionsMenu();
        progressBar.setVisibility(View.VISIBLE);


        final Point pt = DeviceInfo.getScreenSize(getContext());
        final int width = pt.x;
        final int height = pt.y;
        final Url url = ImageUtils.getBestFitImageUrl(width, data.getUrls());
        Uri uri = Uri.parse(url.getUrl());



        imageLoader.loadImage(uri, photo, imageLoadTag, true, new ImageLoader.ImageLoaderListener() {

            @Override
            public void onImageLoadSuccess() {
                photoAttacher.update();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onImageLoadError() {
                progressBar.setVisibility(View.GONE);
            }
        });



    }






    public interface OnPhotoPagerItemFragmentInteractionListener {

    }
}
