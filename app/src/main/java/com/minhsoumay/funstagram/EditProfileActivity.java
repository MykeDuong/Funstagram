/*
 * This class is our EditProfileActivity which allows the user to edit
 * his/her profile by changing the username, the profile image and the
 * bio.
 */
package com.minhsoumay.funstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import com.minhsoumay.funstagram.Model.User;
import com.minhsoumay.funstagram.Runnable.PlaybackRunnable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView close;
    private CircleImageView imageProfile;
    private TextView save;
    private TextView changePhoto;
    private EditText username;
    private EditText bio;

    private TextView changeAudio;

    private Button male;
    private Button female;
    private Button fantasy;
    private Button haunted;
    private Button maleTry;
    private Button femaleTry;
    private Button fantasyTry;
    private Button hauntedTry;

    private String chosenAudio;

    private FirebaseUser fUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static String currentPhotoPath;
    static boolean flag_new = false;

    /**
     * This method is the on create method
     * which sets the content view and
     * displays the content. Here we are doing the majority of the
     * work by getting various component information from the firebase
     * and setting them in their appropriate fields and then
     * setting the onCLick property for changing the image, closing
     * the page and saving.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        close = findViewById(R.id.close);
        imageProfile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        changePhoto = findViewById(R.id.change_photo);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);

        changeAudio = findViewById(R.id.change_audio);

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        fantasy = findViewById(R.id.fantasy);
        haunted = findViewById(R.id.haunted);

        maleTry = findViewById(R.id.male_try);
        femaleTry = findViewById(R.id.female_try);
        fantasyTry = findViewById(R.id.fantasy_try);
        hauntedTry = findViewById(R.id.haunted_try);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                chosenAudio = user.getAudioChoice();
                System.out.println(chosenAudio);
                updateAudioText();
                Picasso.get().load(user.getImageurl()).into(imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        currentPhotoPath = photoFile.getAbsolutePath();
                    } catch (IOException ex) {
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                "com.minhsoumay.funstagram",
                                photoFile);
                        mImageUri = photoURI;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        flag_new=true;
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

<<<<<<< HEAD
    private void updateAudioText() {
        if (chosenAudio == null) {
            changeAudio.setText("Change Welcome Voice - No choice saved");
        } else {
            changeAudio.setText("Change Welcome Voice - Current : " + chosenAudio);
        }
    }

    public void onClickChangeAudio(View view) {
        Button b = (Button) view;
        String targetAudio = b.getText().toString();
        if (targetAudio.equals("REMOVE VOICE")) {
            chosenAudio = null;
        } else {chosenAudio = targetAudio;}
        updateAudioText();
    }

    /**
     * This method is called when the first sound button is clicked. It
     * call the playback function below, which will create a runnable object
     * to play the sound.
     * @param view
     */
    public void onClickButtonMaleTry(View view) {
        playback(R.raw.male);
    }

    /**
     * This method is called when the second sound button is clicked. It
     * call the playback function below, which will create a runnable object
     * to play the sound.
     * @param view
     */
    public void onClickButtonFemaleTry(View view) {
        playback(R.raw.female);
    }

    /**
     * This method is called when the third sound button is clicked. It
     * call the playback function below, which will create a runnable object
     * to play the sound.
     * @param view
     */
    public void onClickButtonFanatasyTry(View view) {
        playback(R.raw.fantasy);
    }

    /**
     * This method is called when the fourth sound button is clicked. It
     * call the playback function below, which will create a runnable object
     * to play the sound.
     * @param view
     */
    public void onClickButtonHauntedTry(View view) {
        playback(R.raw.haunted);
    }

    /**
     * This method is used to play the sound effect, by creating a new
     * PlaybackRunnable object (given in this project), which will then
     * play the sound in a new thread.
     * @param res
     */
    public void playback(int res) {
        PlaybackRunnable pr = new PlaybackRunnable(this,
                getApplicationContext(),
                res);
        (new Thread(pr)).start();
    }
=======
    /**
     * This function is used to create a image file in the form of a JPEG. It also
     * uses the current date to create a unique file name each time.
     * @return image
     * @throws IOException
     */
>>>>>>> soma-branch

    public File createImageFile() throws IOException {
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

    /**
     * This method is responsible for updating the username and the bio
     * for a particular user on the firebase with the use of a HashMap.
     */

    private void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username.getText().toString());
        map.put("bio", bio.getText().toString());
        map.put("audioChoice", chosenAudio);

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).updateChildren(map);
    }

    /**
     * This method is responsible for uploading the image to the firebase
     * and obtaining the download URL.
     */

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (mImageUri != null) {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpeg");

            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).child("imageurl").setValue(url);
                        pd.dismiss();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The onActivityResult is responsible for actually setting up our image profile
     * by utilizing the currentPhotoPath.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options bmOptions =
                new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(
                currentPhotoPath, bmOptions);

        ImageView imageView = (ImageView) findViewById(R.id.image_profile);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            if(flag_new==true) {
                imageView.setImageBitmap(bitmap);
            }

            uploadImage();
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}