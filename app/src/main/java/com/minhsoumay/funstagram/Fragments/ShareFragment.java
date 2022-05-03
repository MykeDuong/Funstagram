package com.minhsoumay.funstagram.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.minhsoumay.funstagram.R;

import java.io.File;
import java.util.ArrayList;


public class ShareFragment extends Fragment {

    public View.OnClickListener containerActivity = null;

    private View inflatedView = null;

    private ListView contactsListView;
    ArrayAdapter<String> contactsAdapter = null;
    private ArrayList<String> contacts = new ArrayList<String>();
    public String final_email_id ="";


    public ShareFragment() {
        // Required empty public constructor
    }

    public void setContainerActivity(View.OnClickListener containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_share, container, false);
        ListView listView = inflatedView.findViewById(R.id.contact_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String present_text = contacts.get(i);
                String name = present_text.substring(0, present_text.indexOf(" :: "));
                String id = present_text.substring(present_text.indexOf(" :: ") + 4);
                System.out.println(name);
                System.out.println(id);
                Cursor emails = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
                if (emails.moveToNext()) {
                    @SuppressLint("Range") String email_id = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    final_email_id=email_id;
                    System.out.println("get the email");
                    System.out.println(final_email_id);
                }
                else{
                    System.out.println("emp email ??");
                    final_email_id=null;
                    System.out.println(final_email_id);
                }
                emails.close();
                Bundle b = getArguments();
                File final_photo_f = (File) b.getSerializable("ImageFile");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("vnd.android.cursor.dir/email");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { final_email_id });
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.minhsoumay.funstagram", final_photo_f);
                intent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });
        return inflatedView;
    }

    /*
     * This method is the on create
     * method.
     */

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getContacts();
    }

    /*
     * This method is the on Resume
     * method.
     */

    @Override
    public void onResume() {
        super.onResume();
        setupContactsAdapter();
    }

    /*
     * This method is responsible for getting the name and ids
     * of all the contacts from our contacts app and add it to
     * the contacts list.
     */

    public void getContacts() {
        int limit = 1000;
        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts._ID));
            @SuppressLint("Range") String given = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contacts.add(given + " :: " + id);
            System.out.println(given);
            System.out.println(id);
        }
        cursor.close();
    }

    /*
     * This method is responsible for setting the contacts
     * ArrayAdapter for the listview to display the contact names
     * and their ids.
     */

    private void setupContactsAdapter() {
        contactsListView =
                (ListView)getActivity().findViewById(R.id.contact_list_view);
        contactsAdapter = new
                ArrayAdapter<String>(getActivity(), R.layout.contact_row,
                R.id.contact_row_text_view, contacts);
        contactsListView.setAdapter(contactsAdapter);
    }
}