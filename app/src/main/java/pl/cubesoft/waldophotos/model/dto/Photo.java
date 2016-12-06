package pl.cubesoft.waldophotos.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

/**
 * Created by CUBESOFT on 02.12.2016.
 */

public class Photo {
    String id;
    List<Url> urls;



    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Photo && id.equals(((Photo)object).getId());
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", urls=" + urls +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public List<Url> getUrls() {
        return urls;
    }
}
