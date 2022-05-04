/*
 * This class is our HomeFragment that represents the home page.
 * Here we are able to see our posts as well as the posts of the
 * people we follow. We can do various things here which include liking,
 * saving and sharing a post.
 */
package com.minhsoumay.funstagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhsoumay.funstagram.Adapter.PostAdapter;
import com.minhsoumay.funstagram.Model.Post;
import com.minhsoumay.funstagram.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;

    /**
     * This method is the on create view method
     * which displays the content. Here we are setting
     * various componenets.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        System.out.println(postAdapter);
        recyclerViewPosts.setAdapter(postAdapter);
        followingList = new ArrayList<>();

        checkFollowingUsers();

        return view;
    }

    /**
     * This method adds the values to the followingList for all the users
     * that are followed by the current user.
     */

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot:datasnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * This method is responsible for geting all the posts of the users in the
     * following list and displaying them in the user's home page.
     */
    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Post post = snap.getValue(Post.class);

                    for(String id:followingList){
                        if(post.getPublisher().equals(id)){
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}