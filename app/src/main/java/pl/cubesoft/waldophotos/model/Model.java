package pl.cubesoft.waldophotos.model;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pl.cubesoft.waldophotos.api.PhotosAPI;
import pl.cubesoft.waldophotos.model.dto.Album;
import rx.Completable;
import rx.Observable;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public class Model {
    private final PhotosAPI photosAPI;

    private final Map<String, Album> albumCache = new HashMap<>();

    public Model(PhotosAPI photosAPI) {
        this.photosAPI = photosAPI;
    }

    public Observable<Album> loadAlbum(boolean force, String albumId, int limit, int offset) {

        // get cached album if exists
        Album cacheEntry = albumCache.get(albumId);
        if (!force && cacheEntry != null) {
            if (cacheEntry.getPhotos().size() >= limit * (offset+1)) {
                return Observable.just(cacheEntry);
            }
        }

        // this is the query to load images
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
        return photosAPI.getAlbum(query).concatMapDelayError(response -> {
            Album result = null;
            // cache the result for further use
            if (!force && cacheEntry != null ) {
                cacheEntry.getPhotos().addAll(response.getAlbum().getPhotos());
                result = cacheEntry;
            } else {
                albumCache.put(response.getAlbum().getId(), result = response.getAlbum());
            }
            return Observable.just(result);
        });

    }
}
