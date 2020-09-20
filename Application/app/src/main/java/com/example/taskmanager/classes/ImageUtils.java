package com.example.taskmanager.classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ImageUtils {

    public static byte[] getImageBytes(ImageView imageView) {

        if(imageView == null) {
            throw new InvalidParameterException("imageView is null");
        }
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getImageBytes(Bitmap bitmap) {
        if(bitmap == null) {
            throw new InvalidParameterException("bitmap is null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static void uploadTaskImageToFirebase(byte[] imageBytes,  IImageUploaded callbacks ) {
        uploadImageToFirebase(imageBytes, "task-pictures", callbacks);
    }

        private static void uploadImageToFirebase(byte[] imageBytes, String folder,  IImageUploaded callbacks ) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(folder).child(UUID.randomUUID().toString());
        UploadTask uploadTask = storageRef.putBytes(imageBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                if (callbacks != null) {
                    callbacks.onFailure(exception);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Task<Uri> taskUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                taskUri.addOnSuccessListener( uri -> {
                    if (callbacks != null) {
                        callbacks.onSuccess(uri.toString());
                    }
                });

                taskUri.addOnFailureListener( ex -> {
                    if (callbacks != null) {
                        callbacks.onFailure(ex);
                    }
                });
            }
        });
    }

    public interface IImageUploaded {
        void onSuccess(String uri);
        void onFailure(Exception ex);
    }

    public static List<String> getUserIcons() {
        return userIcons;
    }

    private static List<String> userIcons = Arrays.asList(
            Constants.USER_ICON_PANDA,
            Constants.USER_ICON_PANDA2,
            Constants.USER_ICON_CHEMICAL,
            Constants.USER_ICON_DEFAULT,
            Constants.USER_ICON_DOCTOR_GIRL,
            Constants.USER_ICON_DOCTOR_GUY,
            Constants.USER_ICON_DUCK,
            Constants.USER_ICON_GAME,
            Constants.USER_ICON_MAN,
            Constants.USER_ICON_WOMEN,
            Constants.USER_ICON_POKEBALL,
            Constants.USER_ICON_POLAR_BEAR,
            Constants.USER_ICON_POLAR_BEAR2,
            Constants.USER_ICON_TIGER);
}
