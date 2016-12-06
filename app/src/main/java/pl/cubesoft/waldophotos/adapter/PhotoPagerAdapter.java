package pl.cubesoft.waldophotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



import java.util.ArrayList;
import java.util.List;

import pl.cubesoft.waldophotos.fragment.PhotoPagerItemFragment;
import pl.cubesoft.waldophotos.model.dto.Photo;


public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private final String imageLoadTag;
    private List<Photo> photos = new ArrayList<>();

    public PhotoPagerAdapter(FragmentManager fm, String imageLoadTag) {
        super(fm);
        this.imageLoadTag =  imageLoadTag;
    }

    public void setData(List<Photo> photos) {
        this.photos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoPagerItemFragment.createInstance(photos.get(position), imageLoadTag);
    }


}
