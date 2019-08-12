package com.example.gsf98.notebook.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class LocationHelper
{
    public static String getLocationInfo(Context context )
    {
        String result = "";
        LocationManager locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
        Location location = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
        if( location == null )
        {
            locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
            location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
        }

        if( location != null )
        {
            result = location.getLatitude() + "," + location.getLongitude();
        }

        return result;
    }

    public static String parseLocation(Context context, String location )
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            double latitude = Double.parseDouble( location.split( "," )[0] );
            double longitude = Double.parseDouble( location.split( "," )[1] );
            List<Address> addresses = new Geocoder( context ).getFromLocation( latitude, longitude, 3 );
            if( addresses != null && addresses.size() > 0 )
            {
                Address address = addresses.get( 0 );
                builder.append( address.getAddressLine( 1 ) );
            }
        }
        catch( Exception e )
        {
        }

        return builder.toString();
    }
}
