package com.minhsoumay.funstagram.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhsoumay.funstagram.Fragments.ShareFragment;
import com.minhsoumay.funstagram.Model.Post;
import com.minhsoumay.funstagram.Model.User;
import com.minhsoumay.funstagram.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Soumay Agarwal
 * COURSE: CSC 317 - Spring 2022
 * @description This class is our PostAdapter which sets up each post of a user and the
 * people he/she follows from the firebase in our Home page.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder>{

    private Context mcontext;
    private List<Post> mposts;
    private FirebaseUser firebaseUser;
    private ArrayList<File> f_list = new ArrayList<File>();

    /**
     * The constructor of our PostAdapter which sets up the
     * context, the list of posts and the current firebase user.
     * @param mcontext
     * @param mposts
     */
    public PostAdapter(Context mcontext, List<Post> mposts) {
        this.mcontext = mcontext;
        this.mposts = mposts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * This method is run when the ViewHolder is created.
     * @param parent    The ViewGroup.
     * @param viewType  The int showing the type of the view.
     * @return
     */
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    /**
     * This is the onBindViewHolder method for our RecyclerView. It helps
     * to attain all the majority of the work for our posts. Here, we
     * load the posts by getting the appropriate ImageURL from the
     * firebase and set the onClick methods for the like, share and
     * save features.
     * @param holder An instance of VieHolder
     * @param position Particular index in the mPosts which is a Posts list
     */
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Post post = mposts.get(position);
        Picasso.get().load(post.getPostimage()).into(holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println(user.getImageurl() + "the image url in post adapt");
                    if (user.getImageurl() == null) {
                        holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                    }
                    holder.username.setText(user.getUsername());
                    holder.author.setText(user.getName());
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isSaved(post.getPostid(), holder.save);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }
            }
        });

        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.num_likes);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag() != null) {
                    if (holder.like.getTag().equals("like")) {
                        FirebaseDatabase.getInstance().getReference().child("Likes")
                                .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Likes")
                                .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    }
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareFragment sf = new ShareFragment();
                ImageView imageView = (ImageView) holder.postImage;
                Bundle bundle = new Bundle();
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Context c = activity.getBaseContext();
                File photo_file = getMainDirectoryName(c);
                File final_photo_f = store(bitmap, "JPEG_21.jpg", photo_file);
                f_list.add(final_photo_f);
                System.out.println(final_photo_f);
                bundle.putSerializable("ImageFile", final_photo_f);
                sf.setArguments(bundle);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, sf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    /**
     * This method checks if a post (and thus an image) has been saved or not.
     * @param postId    The id of the post to check
     * @param image     The image of the post.
     */
    private void isSaved (final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(postId + "in save");
                if (postId != null) {
                    if (dataSnapshot.child(postId).exists()) {
                        image.setImageResource(R.drawable.ic_save_black);
                        image.setTag("saved");
                    } else {
                        image.setImageResource(R.drawable.ic_save);
                        image.setTag("save");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**
     * This method is responsible for getting the no. of likes of a particular post
     * from the firebase and then displaying it to the user.
     * @return int
     */
    @Override
    public int getItemCount() {
        return mposts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView share;
        public ImageView save;

        public TextView username;
        public TextView num_likes;
        public TextView author;
        TextView description;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like_but);
            share = itemView.findViewById(R.id.share);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            num_likes = itemView.findViewById(R.id.total_likes);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);


        }
    }

    /**
     * This method is responsible for getting the no. of likes of a particular post
     * from the firebase and then displaying it to the user.
     * @param postId The id of a particular post
     * @param imageView Provided ImageView for like whose like needs to be set
     */

    private void isLiked(String postId, final ImageView imageView) {
        if (postId != null) {
            FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        imageView.setImageResource(R.drawable.ic_liked);
                        imageView.setTag("liked");
                    } else {
                        imageView.setImageResource(R.drawable.ic_like);
                        imageView.setTag("like");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * This method is responsible for getting the no. of likes of a particular post
     * from the firebase and then displaying it to the user.
     * @param postId The id of a particular post
     * @param text Provided TextView for number of likes whose number of likes need to be set
     */
    private void noOfLikes (String postId, final TextView text) {
        if (postId != null) {
            FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    text.setText(dataSnapshot.getChildrenCount() + " likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * This method is responsible for storing the bitmap info in
     * a file and returning it.
     * @param bm            The bitmap of the image
     * @param fileName      The name of the file
     * @param saveFilePath  The path that the file is saved
     */
    public static File store(Bitmap bm, String fileName, File saveFilePath) {
        File dir = new File(saveFilePath.getAbsolutePath());
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(saveFilePath.getAbsolutePath(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * This method is responsible for getting the main directory for
     * storing.
     * @param context   The Context of the app.
     */
    public static File getMainDirectoryName(Context context) {
        File mainDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Demo");

        if (!mainDir.exists()) {
            if (mainDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mainDir);
        }
        return mainDir;
    }
}
