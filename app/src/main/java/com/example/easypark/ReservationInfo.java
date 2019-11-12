package com.example.easypark;

import com.google.android.gms.maps.model.LatLng;

public class ReservationInfo {
    public String parkingLotName;
    public LatLng latLng;

    public ReservationInfo(String parkingLotName, LatLng latLng) {
        this.parkingLotName = parkingLotName;
        this.latLng = latLng;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
