package com.example.admin_comic.BUS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Bitmap getImage(byte[]image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
    public static byte[]getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int size=1024;
        byte[]buffer=new byte[size];
        int len=0;
        while ((len=inputStream.read(buffer))!=-1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
