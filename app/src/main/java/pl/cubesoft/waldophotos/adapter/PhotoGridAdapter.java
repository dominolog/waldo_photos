package pl.cubesoft.waldophotos.adapter;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.waldophotos.R;
import pl.cubesoft.waldophotos.core.ImageLoader;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.model.dto.Photo;
import pl.cubesoft.waldophotos.model.dto.Url;
import pl.cubesoft.waldophotos.utils.DeviceInfo;
import pl.cubesoft.waldophotos.utils.ImageUtils;


public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {


    private final int columnCount;
    private final int cellWidth;
    private final int cellHeight;
    private Album album;
    private int screenWidth = 0;
    private final ImageLoader imageLoader;

    private final Object tag;

    private List<Photo> items = new ArrayList<>();
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public void setData(Album data) {
        this.album = data;
        items = data.getPhotos() != null ? data.getPhotos() : Collections.EMPTY_LIST;
        notifyDataSetChanged();
    }




    public List<Photo> getItems() {
        return items;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String id);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position, String id);
    }

    //private Set<Integer> mSelectedItemSet = new HashSet<Integer>();

    public PhotoGridAdapter(Context context, ImageLoader imageLoader, Object tag, int columnCount) {
        this.imageLoader = imageLoader;
        this.tag = tag;
        this.columnCount = columnCount;
        Point screenSize = DeviceInfo.getScreenSize(context);
        cellWidth = screenSize.x / columnCount;
        cellHeight = screenSize.y / columnCount;
        screenWidth = screenSize.x;
        setHasStableIds(true);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photogrid_item, parent, false);

        return new PhotoGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Photo item = getItem(position);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        final int cellWidth = screenWidth / columnCount;

        // get the best fit photos
        final Url url = ImageUtils.getBestFitImageUrl(cellWidth, item.getUrls());


        final int width = url.getWidth();
        final int height = url.getHeight();


        //params.height = height * cellWidth / (width > 0 ? width : 1);
        params.width = params.height = cellWidth;
        holder.itemView.setLayoutParams(params);

        imageLoader.loadImage(Uri.parse(url.getUrl()), holder.photo, tag, false, null);


        holder.itemView.setOnClickListener(view -> {


            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position, item.getId());
            }
        });

        holder.itemView.setOnLongClickListener(view -> {

            if (onItemLongClickListener != null) {
                return onItemLongClickListener.onItemLongClick(view, position, item.getId());
            }
            return false;
        });


    }

    public Photo getItem(Integer position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }





}
