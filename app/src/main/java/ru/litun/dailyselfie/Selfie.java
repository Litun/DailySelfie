package ru.litun.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Litun on 21.08.2015.
 */
public class Selfie implements Parcelable {

    private String name;
    private String photoPath;
    private Bitmap thumbnail;

    public Selfie(String photoPath) {
        this.photoPath = photoPath;
        setThumbnail();
    }

    public Selfie(String photoPath, String name) {
        this.photoPath = photoPath;
        this.name = name;
        setThumbnail();
    }

    protected Selfie(Parcel in) {
        name = in.readString();
        photoPath = in.readString();
        setThumbnail();
    }

    public static final Creator<Selfie> CREATOR = new Creator<Selfie>() {
        @Override
        public Selfie createFromParcel(Parcel in) {
            return new Selfie(in);
        }

        @Override
        public Selfie[] newArray(int size) {
            return new Selfie[size];
        }
    };

    public String getPhotoPath() {
        return photoPath;
    }

    public Bitmap getThumbnail() {
        if (thumbnail == null)
            setThumbnail();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photoPath);
    }
}
