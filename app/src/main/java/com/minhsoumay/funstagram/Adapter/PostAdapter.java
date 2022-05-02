package com.minhsoumay.funstagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhsoumay.funstagram.Model.Post;
import com.minhsoumay.funstagram.Model.User;
import com.minhsoumay.funstagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder>{

    private Context mcontext;
    private List<Post> mposts;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mcontext, List<Post> mposts) {
        this.mcontext = mcontext;
        this.mposts = mposts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Post post = mposts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println(user.getImageurl() + "the image url in post adapt");
                if (user.getImageurl() != null) {
                    if (user.getImageurl().equals("default")) {
                        holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                    }
                    holder.username.setText(user.getUsername());
                    holder.author.setText(user.getName());
                }
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
    }


    private void isSaved (final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mposts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView num_likes;
        public TextView author;
        public TextView num_comments;
        EditText description;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like_but);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username);
            num_likes = itemView.findViewById(R.id.total_likes);
            author = itemView.findViewById(R.id.author);
            num_comments = itemView.findViewById(R.id.total_comments);
            description = itemView.findViewById(R.id.description);


        }
    }

    private void isLiked(String postId, final ImageView imageView) {
        System.out.println(postId + "in like");
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
}
