package com.example.easypark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ReservationListAdapter extends ArrayAdapter<ReservationInfo> {
    private Context mContext;
    int mResource;


    public ReservationListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ReservationInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the res info
        String parkingName = getItem(position).getParkingLotName();
        LatLng latLng = getItem(position).getLatLng();

        // create the res object with the info
        ReservationInfo reservationInfo = new ReservationInfo(parkingName, latLng);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.parkingName);
        TextView tvLatLng = (TextView) convertView.findViewById(R.id.latlong);

        tvName.setText(parkingName);
        tvLatLng.setText((latLng).toString());

        return convertView;

    }
}
