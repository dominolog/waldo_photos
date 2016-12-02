package pl.cubesoft.waldophotos.model.dto;

import java.util.List;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public class Album {
    String id;
    String name;


    public class PhotosRecord {
        List<Photo> records;
    }
    PhotosRecord photos;

    public List<Photo> getPhotos() {
        return photos.records;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photos=" + photos +
                '}';
    }
}
