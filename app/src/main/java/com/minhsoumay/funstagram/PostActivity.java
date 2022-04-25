package com.minhsoumay.funstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity {

    private Uri imageUri;

    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    EditText description;

    Activity cropImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        startActivity(new Intent(PostActivity.this, CropActivity.class));
        //CropImageActivity.start(PostActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //CropImage.ActivityResult result = CropActivity.getActivityResult()
            //imageUri =
        }
    }

}