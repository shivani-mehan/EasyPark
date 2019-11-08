package com.example.easypark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendorWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.info_title);

        String body = marker.getSnippet();
        TextView tvBody = (TextView) view.findViewById(R.id.info_body);

        if(title != null && !title.isEmpty()){
            tvTitle.setText(title);
        }

        if(body != null && !body.isEmpty()){
            tvBody.setVisibility(View.VISIBLE);
            tvBody.setText(body);
        } else {
            tvBody.setVisibility(View.GONE);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendorWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendorWindowText(marker, mWindow);
        return mWindow;
    }
}
