package com.muhibbin.expensenote.ShareNote;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.NoteCreateDialogFragment;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.NoteCreateListener;
import com.muhibbin.expensenote.Features.NotesCRUD.ShowNoteList.NoteListRecyclerViewAdapter;
import com.muhibbin.expensenote.Login.LogInActivity;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;
import com.muhibbin.expensenote.model.Contact;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SharedNoteListActivity extends AppCompatActivity implements NoteCreateListener {

    private static final String TAG = "SharedNoteListActivity";
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Note> noteList = new ArrayList<>();

    private TextView summaryTextView;
    private LinearLayout noteListEmptyTextView;

    private RecyclerView recyclerView;
    public static SharedNoteListRecyclerViewAdapter noteListRecyclerViewAdapter;

    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_shared_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        noteListEmptyTextView = findViewById(R.id.emptyListTextView);


//        noteList.addAll(databaseQueryClass.getAllNote());

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", NULL);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onDataChange: " + data.child("phone").getValue());
//                    if (contactList.get(i).phoneNumber.contains(data.child("phone").getValue().toString()) || data.child("phone").getValue().toString().contains(contactList.get(i).phoneNumber)) {
//                        //do ur stuff
//                        Log.d(TAG, "onDataChange: " + data.getValue());
//                        contactListFinal.add(contactList.get(i));
//
//                    } else {
//                        //do something if not exists
//
//                    }
//
//                }
//                noteListRecyclerViewAdapter = new NoteListRecyclerViewAdapter(getApplicationContext(), noteList);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                recyclerView.setAdapter(noteListRecyclerViewAdapter);
//
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
                    if (data.child("phone").getValue().toString().contains(phoneNumber)) {
                        //do ur stuff
                        Map<String, Object> map  = (Map) data.child("shared_list").getValue();
                        int i = 0;
                        for (String key: map.keySet()) {
                            Map<String, Object> map1  = (Map<String, Object>) map.get(key);
                            Note note = new Note(i,map1.get("title").toString());
                            noteList.add(note);

                            for (String key1: map1.keySet()) {
//                                Log.d(TAG, "onDataChange: key :"+ map1.get(key1));
//                                Log.d(TAG, "onDataChange: value :"+  map1.get(key1));

                            }
                            i++;
                        }
//                        Note note= new Note(i,data.child("shared_list").child());
//                        noteList.add(note);

                    } else {
                        //do something if not exists

                    }
                }
                noteListRecyclerViewAdapter = new SharedNoteListRecyclerViewAdapter(getApplicationContext(), noteList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(noteListRecyclerViewAdapter);

                viewVisibility();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNoteCreateDialog();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        printSummary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
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


    public void viewVisibility() {
        if (noteList.isEmpty())
            noteListEmptyTextView.setVisibility(View.VISIBLE);
        else
            noteListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    private void openNoteCreateDialog() {
        NoteCreateDialogFragment noteCreateDialogFragment = NoteCreateDialogFragment.newInstance("Create List", this);
        noteCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_NOTE);
    }

    private void printSummary() {
        long noteNum = databaseQueryClass.getNumberOfNote();
        long subjectNum = databaseQueryClass.getNumberOfSubject();

        summaryTextView.setText(getResources().getString(R.string.database_summary, noteNum, subjectNum));
    }

    @Override
    public void onNoteCreated(Note note) {
        noteList.add(note);
        noteListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(note.getName());
    }


}
