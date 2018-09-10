package pl.cubesoft.waldophotos.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumResponse{

    Data data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        Album album;
    }

    public Album getAlbum() {
        return data.album;
    }
}
