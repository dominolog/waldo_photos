package pl.cubesoft.waldophotos.model.dto;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public class AlbumResponse{
    Data data;
    public class Data {
        Album album;
    }

    public Album getAlbum() {
        return data.album;
    }
}
