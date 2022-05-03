package com.minhsoumay.funstagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.minhsoumay.funstagram.Adapter.UserAdapter;
import com.minhsoumay.funstagram.Model.User;
import com.minhsoumay.funstagram.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> mUsers;
    private EditText searchBar;
    private UserAdapter userAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<User>();
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerView.setAdapter(userAdapter);

        searchBar = view.findViewById(R.id.search_bar);

        readUsers();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(searchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (TextUtils.isEmpty(searchBar.getText().toString())) {
                    mUsers.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUser(String s) {
        System.out.println(s);
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);
                    System.out.println(user.getUsername());
                    System.out.println(mUsers);
                }

                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}