package com.example.gsf98.notebook.view;

import java.util.HashMap;
import java.util.Map;

public class Icons
{
    private static Map<String, String> faMap = new HashMap<String, String>();

    static
    {
        // mood
        faMap.put( "fb-happy", "\ue600" );
        faMap.put( "fb-smiley", "\ue601" );
        faMap.put( "fb-tongue", "\ue602" );
        faMap.put( "fb-sad", "\ue603" );
        faMap.put( "fb-wink", "\ue604" );
        faMap.put( "fb-grin", "\ue605" );
        faMap.put( "fb-cool", "\ue606" );
        faMap.put( "fb-wondering", "\ue607" );
        faMap.put( "fb-neutral", "\ue608" );
        faMap.put( "fb-confused", "\ue609" );
        faMap.put( "fb-shocked", "\ue60a" );
        faMap.put( "fb-evil", "\ue60b" );
        faMap.put( "fb-angry", "\ue60c" );

        // tag
        faMap.put( "fb-headphones", "\ue60d" );
        faMap.put( "fb-book", "\ue60e" );
        faMap.put( "fb-cart", "\ue60f" );
        faMap.put( "fb-hammer", "\ue610" );
        faMap.put( "fb-bug", "\ue611" );
        faMap.put( "fb-trophy", "\ue612" );
        faMap.put( "fb-food", "\ue613" );
        faMap.put( "fb-mug", "\ue614" );
        faMap.put( "fb-airplane", "\ue615" );
        faMap.put( "fb-trunk", "\ue616" );
        faMap.put( "fb-briefcase", "\ue617" );
        faMap.put( "fb-accessibility", "\ue618" );
        faMap.put( "fb-star", "\ue619" );
        faMap.put( "fb-heart", "\ue61a" );

        // weather
        faMap.put( "fb-sun", "\ue61b" );
        faMap.put( "fb-snowflake", "\ue61c" );
        faMap.put( "fb-cloudy", "\ue61d" );
        faMap.put( "fb-cloud", "\ue61e" );
        faMap.put( "fb-weather", "\ue61f" );
        faMap.put( "fb-weather2", "\ue620" );
        faMap.put( "fb-weather3", "\ue621" );
        faMap.put( "fb-weather4", "\ue622" );
        faMap.put( "fb-snowy", "\ue623" );
        faMap.put( "fb-snowy2", "\ue624" );
        faMap.put( "fb-snowy3", "\ue625" );
        faMap.put( "fb-windy", "\ue626" );
        faMap.put( "fb-windy2", "\ue627" );
        faMap.put( "fb-rainy", "\ue628" );
        faMap.put( "fb-lightning", "\ue629" );
        faMap.put( "fb-rainy2", "\ue62a" );
        faMap.put( "fb-cloud2", "\ue62b" );
        faMap.put( "fb-lightning2", "\ue62c" );
        faMap.put( "fb-lightning3", "\ue62d" );
        faMap.put( "fb-cloud3", "\ue62e" );
        faMap.put( "fb-cloudy2", "\ue62f" );

        // type
        faMap.put( "fb-image", "\ue630" );
        faMap.put( "fb-tag", "\ue631" );
        faMap.put( "fb-camera", "\ue632" );
        faMap.put( "fb-camera2", "\ue633" );
        faMap.put( "fb-location", "\ue634" );
        faMap.put( "fb-bullhorn", "\ue635" );
        faMap.put( "fb-file", "\ue636" );

        // weather2
        faMap.put( "fb-sun-o", "\ue637" );
        faMap.put( "fb-cloudy-o", "\ue638" );
        faMap.put( "fb-rainy-o", "\ue639" );
        faMap.put( "fb-snowy-o", "\ue63a" );
        faMap.put( "fb-lightning-o", "\ue63b" );

        // operation
        faMap.put( "fb-plus", "\ue63c" );
        faMap.put( "fb-minus", "\ue63d" );
        faMap.put( "fb-checkmark", "\ue63e" );
        faMap.put( "fb-close", "\ue63f" );
    }

    public static Map<String, String> getFaMap()
    {
        return faMap;
    }

}
