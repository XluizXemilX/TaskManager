package com.example.taskmanager.classes;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.taskmanager.R;

public class DataBindingAdapters {

   @BindingAdapter("android:src")
   public static void setImageUri(ImageView view, String imageUri) {
       if (imageUri == null) {
           view.setImageURI(null);
       } else {
           view.setImageURI(Uri.parse(imageUri));
       }
   }

    @BindingAdapter("android:src")
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
            case Constants.USER_ICON_DEFAULT:
                imageView.setImageResource(R.drawable.user_default_icon);
                break;
            case Constants.USER_ICON_CHEMICAL:
                imageView.setImageResource(R.drawable.user_icon_chemical);
                break;
            case Constants.USER_ICON_POKEBALL:
                imageView.setImageResource(R.drawable.pokeball_icon);
                break;
            case Constants.USER_ICON_PANDA:
                imageView.setImageResource(R.drawable.panda_icon);
                break;
            case Constants.USER_ICON_PANDA2:
                imageView.setImageResource(R.drawable.panda_icon_2);
                break;
            case Constants.USER_ICON_DOCTOR_GIRL:
                imageView.setImageResource(R.drawable.doctor_icon_2);
                break;
            case Constants.USER_ICON_DOCTOR_GUY:
                imageView.setImageResource(R.drawable.doctor_icon);
                break;
            case Constants.USER_ICON_GAME:
                imageView.setImageResource(R.drawable.game_icon);
                break;
            case Constants.USER_ICON_POLAR_BEAR:
                imageView.setImageResource(R.drawable.polarbear_icon);
                break;
            case Constants.USER_ICON_POLAR_BEAR2:
                imageView.setImageResource(R.drawable.polarbear_icon_2);
                break;
            case Constants.USER_ICON_TIGER:
                imageView.setImageResource(R.drawable.tiger_icon);
                break;
            case Constants.USER_ICON_WOMEN:
                imageView.setImageResource(R.drawable.women_icon_1);
                break;
            case Constants.USER_ICON_MAN:
                imageView.setImageResource(R.drawable.man_icon_1);
                break;
            case Constants.USER_ICON_DUCK:
                imageView.setImageResource(R.drawable.duck_icon);
                break;

        }

    }
}