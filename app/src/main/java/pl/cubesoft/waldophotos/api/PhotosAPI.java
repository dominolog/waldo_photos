package pl.cubesoft.waldophotos.api;

import io.reactivex.Observable;
import pl.cubesoft.waldophotos.model.dto.AlbumResponse;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public interface PhotosAPI {

    @POST("gql")
    Observable<AlbumResponse> getAlbum(@Query("query") String query);

}
