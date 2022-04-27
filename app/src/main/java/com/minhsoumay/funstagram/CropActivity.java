package com.minhsoumay.funstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.canhub.cropper.CropImageActivity;

public class CropActivity extends CropImageActivity {

    com.canhub.cropper.CropImageView cropImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropImageView = findViewById(R.id.crop_image_view);

        setCropImageView(cropImageView);

        // cropImageView.setOnCropImageCompleteListener()
    }

}