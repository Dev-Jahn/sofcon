package com.example.kyeon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public InfoWindowCustom(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.marker_window, null);

        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvSnippet = (TextView)view.findViewById(R.id.tvSnippet);
        TextView tvScore = (TextView)view.findViewById(R.id.tvScore);
        ImageButton ibDelete = (ImageButton)view.findViewById(R.id.deleteImageButton);

        tvTitle.setText(marker.getTitle());
        tvSnippet.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData)marker.getTag();
        tvScore.setText(infoWindowData.getScore());

        ibDelete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * delete marker . . . How?
                 * reference :
                 * http://www.zoftino.com/google-maps-android-custom-info-window-example
                 */
            }
        });

        return view;
    }

}
