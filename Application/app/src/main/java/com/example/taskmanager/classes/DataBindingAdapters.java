package com.example.taskmanager.classes;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.taskmanager.R;

public class DataBindingAdapters {

    //@BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    //@BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    //@BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @BindingAdapter("app:userIcon")
    public static void setImageResourceByName(ImageView imageView, String icon){

        if (TextUtils.isEmpty(icon)) {
            return;
        }

        switch (icon)  {
            case "DEFAULT_USER_ICON":
                imageView.setImageResource(R.drawable.user_default_icon);
                break;
        }

    }
}