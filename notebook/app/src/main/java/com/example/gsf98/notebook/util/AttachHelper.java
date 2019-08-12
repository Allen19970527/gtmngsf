package com.example.gsf98.notebook.util;

import com.kbeanie.imagechooser.api.FileUtils;
import com.example.gsf98.notebook.entity.Tag;

import java.util.List;

public class AttachHelper
{
    public static String parseSmallThumb(String filename )
    {
        String dir = FileUtils.getDirectory( Constant.SD_PATH );
        filename = filename.replace( ".", "_fact_2." );
        filename = dir + "/" + filename;
        return filename;
    }

    public static String parseOrgThumb(String filename )
    {
        String dir = FileUtils.getDirectory( Constant.SD_PATH );
        filename = dir + "/" + filename;
        return filename;
    }

    public static String parseAudio(String filename )
    {
        String dir = FileUtils.getDirectory( Constant.SD_PATH );
        filename = dir + "/" + filename;
        return filename;
    }

    public static String retriveTag(List<Tag> tags )
    {
        StringBuffer sb = new StringBuffer();
        if( tags != null && tags.size() > 0 )
        {
            for( Tag tag : tags )
            {
                sb.append( tag.getName() );
                sb.append( "," );
            }

            return "# " + sb.deleteCharAt( sb.length() - 1 ).toString();
        }

        return sb.toString();
    }
}
