package com.muhibbin.expensenote.Features.NotesCRUD.ShowNoteList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.NoteCreateDialogFragment;
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.NoteCreateListener;
import com.muhibbin.expensenote.Login.LogInActivity;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.ShareNote.ContactListActivity;
import com.muhibbin.expensenote.ShareNote.SharedNoteListActivity;
import com.muhibbin.expensenote.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NoteCreateListener,NavigationView.OnNavigationItemSelectedListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Note> noteList = new ArrayList<>();

    private TextView summaryTextView;
    private LinearLayout noteListEmptyTextView;

    private RecyclerView recyclerView;
    public static NoteListRecyclerViewAdapter noteListRecyclerViewAdapter;

    DrawerLayout drawerLayout;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        noteListEmptyTextView = findViewById(R.id.emptyListTextView);

        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        navigationView.setItemIconTintList(null);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_list);


        noteList.addAll(databaseQueryClass.getAllNote());

        noteListRecyclerViewAdapter = new NoteListRecyclerViewAdapter(this, noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(noteListRecyclerViewAdapter);

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

        if(item.getItemId()== R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all notes?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllNotes();
                            if(isAllDeleted){
                                noteList.clear();
                                noteListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(noteList.isEmpty())
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_login:
                Intent logInActivity = new Intent(this, LogInActivity.class);
                startActivity(logInActivity);
                break;
                case R.id.nav_list:
//                Intent noteListActivity = new Intent(this, NoteListActivity.class);
//                startActivity(noteListActivity);
                break;
                case R.id.nav_shared_list:
                Intent noteListActivity = new Intent(this, SharedNoteListActivity.class);
                startActivity(noteListActivity);
                break;
            case R.id.nav_trash:
//                tabs.getTabAt(0).select();
                Toast.makeText(this, "Trash", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_remove_ads:
                Toast.makeText(this, "Remove ads", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
