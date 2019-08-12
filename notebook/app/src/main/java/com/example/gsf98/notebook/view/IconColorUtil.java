package com.example.gsf98.notebook.view;

import com.example.gsf98.notebook.R;

import java.util.HashMap;
import java.util.Map;

public class IconColorUtil
{
    private static Map<String, int[]> faMap = new HashMap<String, int[]>();

    static
    {
        // type
        faMap.put( "fb-file", new int[]{ R.color.clr_tag1 } );
        faMap.put( "fb-image", new int[]{ R.color.clr_tag2 } );
        faMap.put( "fb-location", new int[]{ R.color.clr_tag3 } );
        faMap.put( "fb-camera2", new int[]{ R.color.clr_tag4 } );
        faMap.put( "fb-bullhorn", new int[]{ R.color.clr_tag5 } );

        // mood
        faMap.put( "fb-happy", new int[]{ R.color.clr_tag6 } );
        faMap.put( "fb-sad", new int[]{ R.color.clr_tag7 } );
        faMap.put( "fb-cool", new int[]{ R.color.clr_tag8 } );
        faMap.put( "fb-wondering", new int[]{ R.color.clr_tag9 } );
        faMap.put( "fb-angry", new int[]{ R.color.clr_tag10 } );

        // weather
        faMap.put( "fb-sun-o", new int[]{ R.color.clr_tag11 } );
        faMap.put( "fb-cloudy-o", new int[]{ R.color.clr_tag12 } );
        faMap.put( "fb-rainy-o", new int[]{ R.color.clr_tag13 } );
        faMap.put( "fb-snowy-o", new int[]{ R.color.clr_tag14 } );
        faMap.put( "fb-lightning-o", new int[]{ R.color.clr_tag15 } );

        // tag
        faMap.put( "fb-headphones", new int[]{ R.color.clr_tag16 } );
        faMap.put( "fb-book", new int[]{ R.color.clr_tag17 } );
        faMap.put( "fb-cart", new int[]{ R.color.clr_tag18 } );
        faMap.put( "fb-hammer", new int[]{ R.color.clr_tag19 } );
        faMap.put( "fb-bug", new int[]{ R.color.clr_tag20 } );
        faMap.put( "fb-trophy", new int[]{ R.color.clr_tag21 } );
        faMap.put( "fb-food", new int[]{ R.color.clr_tag22 } );
        faMap.put( "fb-mug", new int[]{ R.color.clr_tag23 } );
        faMap.put( "fb-airplane", new int[]{ R.color.clr_tag24 } );
        faMap.put( "fb-trunk", new int[]{ R.color.clr_tag25 } );
        faMap.put( "fb-briefcase", new int[]{ R.color.clr_tag26 } );
        faMap.put( "fb-accessibility", new int[]{ R.color.clr_tag27 } );
        faMap.put( "fb-star", new int[]{ R.color.clr_tag28 } );
        faMap.put( "fb-heart", new int[]{ R.color.clr_tag29 } );
    }

    public static Map<String, int[]> getFaMap()
    {
        return faMap;
    }
}
