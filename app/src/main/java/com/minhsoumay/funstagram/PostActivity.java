package com.minhsoumay.funstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;
    StorageTask uploadTask;
    StorageReference storageReference;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File photoFile;
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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });


        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    private void upload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        System.out.println(imageUri + "the im uri");
        if (imageUri != null){
            System.out.println(storageReference);
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    System.out.println("complete");
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("postid" , postid);
                        hashMap.put("postimage" , imageUrl);
                        hashMap.put("description" , description.getText().toString());
                        hashMap.put("publisher" , FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(PostActivity.this , MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(PostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }

    /**
     * This function is called when one of the four partial images is pressed.
     * It leads the user to the camera activity (default, of the device), with the
     * photoFile and photoURI being the file and URI of the to-be-captured picture.
     * @param view
     */
    public void imagePressed(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageAdded = (ImageView) view;
        // Create the File where the photo should go
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Continue if the File was successfully created
        if (photoFile != null) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.minhsoumay.funstagram",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * This function is called after the camera activity is done and the picture
     * is taken. It will crop the picture (the two side if horizontal, the top and bottom
     * if vertical) to make the picture become a square, and set it in place of the current
     * partial image of the collage.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            // Crop the bitmap to be squared, depending on if it is horizontal or vertical
            Bitmap finalBmp;
            if (bitmap.getWidth() >= bitmap.getHeight()) {
                finalBmp = Bitmap.createBitmap(
                        bitmap,
                        bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                        0,
                        bitmap.getHeight(),
                        bitmap.getHeight()
                );
            } else {
                finalBmp = Bitmap.createBitmap(
                        bitmap,
                        0,
                        bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                        bitmap.getWidth(),
                        bitmap.getWidth()
                );
            }
            imageAdded.setImageBitmap(bitmap);
        }
    }

    /**
     * This function is used to create a image file in the form of a JPEG. It also
     * uses the current date to create a unique file name each time.
     * @return image
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}