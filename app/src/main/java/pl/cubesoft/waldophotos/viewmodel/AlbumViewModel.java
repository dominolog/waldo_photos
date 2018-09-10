package pl.cubesoft.waldophotos.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import pl.cubesoft.waldophotos.model.dto.Album;

public class AlbumViewModel extends ViewModel {

    private final AlbumRepository repository;

    public AlbumViewModel(AlbumRepository repository){
        this.repository = repository;
    }

    public MutableLiveData<Album> getData(String id) {
        return repository.getData(id);
    }

    public MutableLiveData<AlbumRepository.State> getState(String id) {
        return repository.getState(id);
    }

    public void loadData(String albumId, int limit, int offset) {
        repository.loadData(albumId, limit, offset);    }
}
