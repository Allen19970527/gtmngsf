package com.example.gsf98.notebook.util;

import com.example.gsf98.notebook.bean.GeoBean;

public class ObjectToStringUtility
{
    public static String toString(GeoBean bean )
    {
        double[] c = bean.getCoordinates();
        return "type=" + bean.getType() + "coordinates=" + "[" + c[0] + "," + c[1] + "]";
    }
}