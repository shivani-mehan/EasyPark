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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        ListView mListView = root.findViewById(R.id.list_view);

        LatLng latlng = new LatLng(43.473044, -80.526571);
        ReservationInfo res1 = new ReservationInfo("Bricker Academic Parking Lot", latlng);

        ArrayList<ReservationInfo> reservationInfoArrayList = new ArrayList<>();

        reservationInfoArrayList.add(res1);

        ReservationListAdapter adapter = new ReservationListAdapter(getContext(), R.layout.adapter_list_view_item, reservationInfoArrayList );
        mListView.setAdapter(adapter);

        return root;
    }
}