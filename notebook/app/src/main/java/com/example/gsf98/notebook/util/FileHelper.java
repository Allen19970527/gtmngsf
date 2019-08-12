package com.example.gsf98.notebook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper
{
    public static File copy(String resultFile, String sourceFile )
    {
        File readFile = new File( sourceFile );
        File writeFile = new File( resultFile );

        if( readFile.exists() && readFile.isFile() )
        {
            FileInputStream readStream = null;
            FileOutputStream writeStream = null;
            byte[] buffer = new byte[10 * 1024];
            try
            {
                readStream = new FileInputStream( readFile );
                writeStream = new FileOutputStream( writeFile );

                do
                {
                    int numread = readStream.read( buffer );
                    if( numread == -1 )
                    {
                        break;
                    }
                    writeStream.write( buffer, 0, numread );
                }
                while( true );
            }
            catch( IOException ioErr )
            {
            }
            catch( Exception err )
            {
            }
            finally
            {
                try
                {
                    readStream.close();
                    writeStream.close();
                }
                catch( IOException ioErr )
                {
                }
            }
        }

        return writeFile;
    }
}
