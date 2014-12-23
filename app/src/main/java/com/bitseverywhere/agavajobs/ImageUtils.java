package com.bitseverywhere.agavajobs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    private ImageUtils(){}

    public static Bitmap getBitmapFromStringBase64(String source) {
        if (source != null && source.length() > 22) {
            try {
                byte[] decodedString = Base64.decode(source.substring(22), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                return decodedByte;
            } catch (Error e) {
                return null;
            }
        }
        return null;
    }

    public static String getStringBase64FromBitmap(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            return "data:image/png;base64," + encodedImage;
        }
        return null;
    }
}
