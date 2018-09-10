package pl.cubesoft.waldophotos.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.cubesoft.waldophotos.api.PhotosAPI;
import pl.cubesoft.waldophotos.model.dto.Album;
import pl.cubesoft.waldophotos.model.dto.AlbumResponse;

import static pl.cubesoft.waldophotos.viewmodel.AlbumRepository.State.LOADED;
import static pl.cubesoft.waldophotos.viewmodel.AlbumRepository.State.LOADING;
import static pl.cubesoft.waldophotos.viewmodel.AlbumRepository.State.LOAD_ERROR;

public class AlbumRepository {

    private final PhotosAPI photosAPI;
    private final CompositeDisposable disposables = new CompositeDisposable();



    public enum State {
        LOADED,
        LOADING,
        LOAD_ERROR,
    }

    class AlbumEntry {
        private final MutableLiveData<Album> album = new MutableLiveData<>();
        private final MutableLiveData<State> state= new MutableLiveData<>();


    }

    private final Map<String, AlbumEntry> albums = new HashMap<>();


    public AlbumRepository(PhotosAPI photosAPI) {

        this.photosAPI = photosAPI;
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    private void clearDisposables() {
        disposables.clear();
    }

    private void setState(String id, State state) {
        getState(id).setValue(state);
    }

    private void setData(String id, AlbumResponse data, int limit, int offset) {

        Album currentData = this.getData(id).getValue();
        if (currentData != null) {
            if (currentData.getPhotos().size() <= offset ) {
                currentData.getPhotos().addAll(data.getAlbum().getPhotos());
            }
        } else {
            currentData = data.getAlbum();
        }
        this.getData(id).setValue(currentData);
    }


    public MutableLiveData<Album> getData(String id) {
        AlbumEntry data = albums.get(id);
        if (data == null) {
            data = new AlbumEntry();
            albums.put(id, data);
        }
        return data.album;
    }



    public MutableLiveData<State> getState(String id) {
        AlbumEntry data = albums.get(id);
        if (data == null) {
            data = new AlbumEntry();
            albums.put(id, data);
        }
        return data.state;
    }

    public void loadData(String albumId, int limit, int offset) {


        final String query = String.format("query {\n" +
                "  album(id: \"%s\") {\n" +
                "    id\n" +
                "    name\n" +
                "    photos(slice: { limit: %d, offset: %d }) {\n" +
                "      records {\n" +
                "        id\n" +
                "        urls {\n" +
                "          size_code\n" +
                "          url\n" +
                "          width\n" +
                "          height\n" +
                "          quality\n" +
                "          mime\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", albumId, limit, offset);

        addDisposable(
                photosAPI.getAlbum(query)
                        //.map(archives -> convert(archives))
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(a -> setState(albumId, LOADING))
                        .subscribe(album -> {
                                    setState(albumId, LOADED);
                                    setData(albumId, album, limit, offset);
                                },
                                throwable -> {
                                    setState(albumId, LOAD_ERROR);
                                }));
    }
}
