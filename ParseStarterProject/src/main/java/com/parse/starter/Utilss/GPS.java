package com.parse.starter.Utilss;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.parse.starter.BaseActivity;
import com.parse.starter.Main.MainActivity;


public class GPS extends BaseActivity implements LocationListener {
    Context mContext;
    Location mlocation;
    LocationManager mLocationManager;
    String mProvider = LocationManager.GPS_PROVIDER;
    LocationListener mLocationListener;




    public GPS(Context mContext) {
        this.mContext = mContext;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);



        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                return;
        }
        else {

            mlocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (mlocation == null) {
                mLocationManager.requestLocationUpdates(mProvider, 0, 0, mLocationListener);
            } else {
                onLocationChanged(mlocation);
            }
        }

    }





    public Location getLocation() {
        return mlocation;
    }

    public int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        int dis = (int) Math.floor(dist);
        if (dis < 1) {
            return 1;
        }

        return dis;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public void onLocationChanged(Location location) {
        mlocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
