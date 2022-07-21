package com.muhibbin.expensenote.ShareNote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.Features.NotesCRUD.ShowNoteList.NoteListRecyclerViewAdapter;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.model.Contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ContactListActivity extends AppCompatActivity implements ContactRecyclerViewAdapter.OnItemClickListener{
    private static final String TAG = "ContactListActivity";
    ArrayList<Contact> contactList = new ArrayList<>();
    ArrayList<Contact> contactListFinal = new ArrayList<>();
    ArrayList<Contact> searchList = new ArrayList<>();

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    FastScrollRecyclerView recyclerView;
    androidx.appcompat.widget.SearchView searchView;
    private LinearLayout noteListEmptyTextView;

    //    private RecyclerView recyclerView;
    public static ContactRecyclerViewAdapter contactRecyclerViewAdapter;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.fast_scroller_recycler);



        searchView = findViewById(R.id.search);
        noteListEmptyTextView = findViewById(R.id.emptyListTextView);

        getContactList();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

//        Query query = mDatabaseRef.orderByChild("phone").equalTo("01521257710");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // dataSnapshot is the "issue" node with all children with id 0
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        // do something with the individual "issues"
//                        Log.d(TAG, "onDataChange: "+data.getValue().toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onDataChange: "+data.child("phone").getValue());
                    for (int i = 0; i < contactList.size(); i++) {
                        if (contactList.get(i).phoneNumber.contains(data.child("phone").getValue().toString()) || data.child("phone").getValue().toString().contains(contactList.get(i).phoneNumber)) {
                            //do ur stuff
                            Log.d(TAG, "onDataChange: " + data.getValue());
                            contactListFinal.add(contactList.get(i));

                        } else {
                            //do something if not exists

                        }
                    }

                }
                loadRV(contactListFinal);
//                contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getApplicationContext(), contactListFinal);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                recyclerView.setAdapter(contactRecyclerViewAdapter);
//                contactRecyclerViewAdapter.notifyDataSetChanged();

                viewVisibility();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    loadRV(contactListFinal);

                } else {
                    searchList.clear();
                    for (int i = 0; i < contactListFinal.size(); i++) {
                        if(contactListFinal.get(i).getName().toUpperCase().startsWith(newText.toUpperCase()) || contactListFinal.get(i).getPhoneNumber().contains(newText)){
                            searchList.add(contactListFinal.get(i));
                        }
                    }
                    loadRV(searchList);
                }
                return false;
            }
        });

    }

    private void loadRV(ArrayList<Contact> contactListRV) {
        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getApplicationContext(), contactListRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(contactRecyclerViewAdapter);
        contactRecyclerViewAdapter.setOnItemClickListener(this);
        contactRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    number = number.replace("-", "");
                    if (!mobileNoSet.contains(number)) {
                        contactList.add(new Contact(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "onCreaterrView  Phone Number: name = " + name
                                + " No = " + number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    public void viewVisibility() {
        if (contactList.isEmpty())
            noteListEmptyTextView.setVisibility(View.VISIBLE);
        else
            noteListEmptyTextView.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String number) {
//        Toast.makeText(this, "afs", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onItemClick: "+contactListFinal.get(position).getPhoneNumber());
        Log.d(TAG, "onItemClick: "+number);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("toNumber",number);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}