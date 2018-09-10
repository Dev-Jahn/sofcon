package com.example.kyeon.myapplication;

import com.google.android.gms.maps.model.Marker;

public class OptimizationData implements Comparable<OptimizationData> {
    private Marker originMarker;
    private Marker destMarker;
    private int originIndex;
    private int destIndex;
    private double distance;

    public OptimizationData(Marker originMarker, Marker destMarker) {
        this.originMarker = originMarker;
        this.destMarker = destMarker;
        originIndex = ((InfoWindowData)originMarker.getTag()).getOrder();
        destIndex = ((InfoWindowData)originMarker.getTag()).getOrder();
    }

    @Override
    public int compareTo(OptimizationData optimizationData) {
        if(this.distance > optimizationData.distance) {
            return -1;
        } else if(this.distance == optimizationData.distance) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setOriginIndex(int originIndex) {
        this.originIndex = originIndex;
    }

    public void setDestIndex(int destIndex) {
        this.destIndex = destIndex;
    }

    public int getOriginIndex() {
        return originIndex;
    }

    public int getDestIndex() {
        return destIndex;
    }

    public Marker getOriginMarker() {
        return originMarker;
    }

    public void setOriginMarker(Marker originMarker) {
        this.originMarker = originMarker;
    }

    public Marker getDestMarker() {
        return destMarker;
    }

    public void setDestMarker(Marker destMarker) {
        this.destMarker = destMarker;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}