/*
 * This class is our PostAdapter which sets up each post of a user
 * for the profile page.
 */
package com.minhsoumay.funstagram.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.minhsoumay.funstagram.Model.Post;
import com.minhsoumay.funstagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    /**
     * The constructor of our PhotoAdapter which sets up the
     * context, the list of posts.
     * @param mContext An instance of Context
     * @param mPosts A list of Posts
     */

    public PhotoAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item, parent, false);
        return  new PhotoAdapter.ViewHolder(view);
    }

    /**
     * This is the onBindViewHolder method for our RecyclerView. Here, we
     * get the post and add it to the ImageView present in our photo_item xml.
     * @param holder An instance of VieHolder
     * @param position Particular index in the mPosts which is a Posts list
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Post post = mPosts.get(position);
        Picasso.get().load(post.getPostimage()).placeholder(R.mipmap.ic_launcher).into(holder.postImage);

    }

    /**
     * This method returns the size of our Posts list.
     */
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
        }
    }


}
