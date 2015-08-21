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

    public void setPic(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        if (bitmap == null ||
                bitmap.getHeight() < targetH ||
                bitmap.getWidth() < targetW) {

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        }
        imageView.setImageBitmap(bitmap);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
