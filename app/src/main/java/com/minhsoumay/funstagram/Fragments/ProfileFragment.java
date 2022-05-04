/*
 * This class is our ProfileFragment that represents the profile page.
 * Here we are able to see all of our own posts and our saved posts.
 * Users are able to edit their profile here as well.
 */
package com.minhsoumay.funstagram.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhsoumay.funstagram.Adapter.PhotoAdapter;
import com.minhsoumay.funstagram.EditProfileActivity;
import com.minhsoumay.funstagram.MainActivity;
import com.minhsoumay.funstagram.Model.Post;
import com.minhsoumay.funstagram.Model.User;
import com.minhsoumay.funstagram.PostActivity;
import com.minhsoumay.funstagram.R;
import com.minhsoumay.funstagram.Runnable.PlaybackRunnable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    private RecyclerView recyclerViewSaves;
    private PhotoAdapter postAdapterSaves;
    private List<Post> mySavedPosts;

    private RecyclerView recyclerView;

    private PhotoAdapter photoAdapter;
    private List<Post> myPhotoList;

    private ImageView imageProfile;
    private TextView followers;
    private TextView following;
    private TextView posts;
    private TextView fullname;
    private TextView bio;
    private TextView username;
    private String chosenAudio;

    private ImageView myPictures;
    private ImageView savedPictures;

    private Button playAudio;
    private Button editProfile;

    private FirebaseUser fUser;
    String profileId;

    /**
     * This method is the on create view method
     * which displays the content. Here we are setting
     * various componenets and configuring the onClick
     * method for the edit profile button, the
     * saved pictures and our own posts or pictures.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment probably because of picasso
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }

        imageProfile = view.findViewById(R.id.image_profile);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        posts = view.findViewById(R.id.posts);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        playAudio = view.findViewById(R.id.play_audio);
        editProfile = view.findViewById(R.id.edit_profile);

        recyclerView = view.findViewById(R.id.recucler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPhotoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), myPhotoList);
        recyclerView.setAdapter(photoAdapter);

        recyclerViewSaves = view.findViewById(R.id.recucler_view_saved);
        recyclerViewSaves.setHasFixedSize(true);
        recyclerViewSaves.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mySavedPosts = new ArrayList<>();
        postAdapterSaves = new PhotoAdapter(getContext(), mySavedPosts);
        recyclerViewSaves.setAdapter(postAdapterSaves);

        userInfo();
        getFollowersAndFollowingCount();
        getPostCount();
        myPhotos();
        getSavedPosts();

        if(profileId.equals(fUser.getUid())){
            editProfile.setText("Edit profile");
        }
        else{
            checkFollowingStatus();
        }

        playAudio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int res = 0;
                System.out.println(chosenAudio);
                if (chosenAudio.equals("Male")) { res = R.raw.male; }
                else if (chosenAudio.equals("Female")) { res = R.raw.female; }
                else if (chosenAudio.equals("Fantasy")) { res = R.raw.fantasy; }
                else if (chosenAudio.equals("Haunted")) { res = R.raw.haunted; }


                if (chosenAudio == null) return;
                PlaybackRunnable pr = new PlaybackRunnable((AppCompatActivity)getActivity(),
                        getContext(),
                        res);
                (new Thread(pr)).start();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = editProfile.getText().toString();

                if (btnText.equals("Edit profile")) {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else {
                    if (btnText.equals("follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).removeValue();
                    }
                }
            }
        });
        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewSaves.setVisibility(View.GONE);

        myPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewSaves.setVisibility(View.GONE);
            }
        });

        savedPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewSaves.setVisibility(View.VISIBLE);
            }
        });




        return view;
    }

    /**
     * This method is responsible for storing all the saved posts by the user to
     * the mySavedPosts Post List.
     */
    private void getSavedPosts() {

        final List<String> savedIds = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Saves").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    savedIds.add(snapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        mySavedPosts.clear();

                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                            Post post = snapshot1.getValue(Post.class);

                            for (String id : savedIds) {
                                if (post.getPostid().equals(id)) {
                                    mySavedPosts.add(post);
                                }
                            }
                        }

                        postAdapterSaves.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is responsible for storing all the user photos
     * the myPhotoList Post List.
     */

    private void myPhotos() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) {
                        myPhotoList.add(post);
                    }
                }
                Collections.reverse(myPhotoList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is responsible for getting and displaying the number of posts
     * of the user.
     */

    private void getPostCount() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) counter ++;
                }

                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is responsible for getting and displaying the followers and the following
     * count of the user.
     */

    private void getFollowersAndFollowingCount() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is responsible for getting and actually displaying all the user
     * info.
     */
    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Picasso.get().load(user.getImageurl()).into(imageProfile);
                username.setText(user.getUsername());
                fullname.setText(user.getName());
                bio.setText(user.getBio());
                chosenAudio = user.getAudioChoice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * This method is responsible for checking whether the current user
     * is following a particular user or not.
     */

    private void checkFollowingStatus() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    editProfile.setText("following");
                } else {
                    editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}