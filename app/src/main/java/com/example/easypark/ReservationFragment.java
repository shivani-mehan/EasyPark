package com.example.easypark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class ReservationFragment extends Fragment {
    String parkingLotName;
    LatLng latLng;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            parkingLotName = bundle.getString("parkingLotName");
            latLng = bundle.getParcelable("latlng");
        }

        // Set Up ListView

        ArrayList<ReservationInfo> reservationInfoArrayList = new ArrayList<>();

        if (parkingLotName != null && latLng != null) {
            ReservationInfo res = new ReservationInfo(parkingLotName, latLng);
            reservationInfoArrayList.add(res);
            ListView mListView = root.findViewById(R.id.list_view);
            ReservationListAdapter adapter = new ReservationListAdapter(getContext(), R.layout.adapter_list_view_item, reservationInfoArrayList );
            mListView.setAdapter(adapter);
        }

        return root;
    }

}