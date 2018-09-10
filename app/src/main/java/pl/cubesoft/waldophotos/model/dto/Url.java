package pl.cubesoft.waldophotos.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Url {
    String size_code;
    String url;
    int width;
    int height;
    float quality;
    String mime;

    public String getSize_code() {
        return size_code;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getQuality() {
        return quality;
    }

    public String getMime() {
        return mime;
    }

    @Override
    public String toString() {
        return "Url{" +
                "size_code='" + size_code + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", quality=" + quality +
                ", mime='" + mime + '\'' +
                '}';
    }
}
