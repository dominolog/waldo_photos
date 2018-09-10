package pl.cubesoft.waldophotos.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AlbumRepository albumRepository;

    public ViewModelFactory(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AlbumViewModel.class)) {
            return (T) new AlbumViewModel(albumRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
