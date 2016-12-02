package pl.cubesoft.waldophotos.api;

import pl.cubesoft.waldophotos.model.dto.AlbumResponse;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public interface PhotosAPI {

    @POST("gql")
    Observable<AlbumResponse> getAlbum(@Query("query") String query);

}
