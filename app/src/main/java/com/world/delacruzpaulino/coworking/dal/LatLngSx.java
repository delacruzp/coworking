package com.world.delacruzpaulino.coworking.dal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by delacruzpaulino on 5/22/16.
 */
public  class LatLngSx implements Serializable {
    private Double lat;
    private Double lon;

    public static LatLngSx creator(LatLng latLng){
        LatLngSx location = new LatLngSx();
        location.lat = latLng.latitude;
        location.lon = latLng.longitude;
        return location;
    }

    @JsonIgnore
    public LatLng getLatLng(){
        LatLng location = new LatLng(this.lat,this.lon);
        return location;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
