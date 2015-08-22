package ru.litun.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * Created by Litun on 21.08.2015.
 */
public class Selfie {

    private String name;
    private String photoPath;
    private Bitmap bitmap;
    private Bitmap thumbnail;

    public Selfie(String photoPath) {
        this.photoPath = photoPath;
        setThumbnail();
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    private void setThumbnail() {
        final int THUMB_MAX_DIM = 384;
        Bitmap decodeBitmap = BitmapFactory.decodeFile(photoPath);
        float ratio = (float) decodeBitmap.getHeight() / decodeBitmap.getWidth();
        int height, width;
        if (ratio > 1f) {
            height = THUMB_MAX_DIM;
            width = (int) (height / ratio);
        } else {
            width = THUMB_MAX_DIM;
            height = (int) (width * ratio);
        }
        thumbnail = ThumbnailUtils.extractThumbnail(decodeBitmap,
                width, height);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
