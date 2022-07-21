package com.muhibbin.expensenote.Features.DetailsCRUD.ShowDetailNoteList;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNote;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNoteCreateDialogFragment;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNoteCreateListener;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNoteCreateSpeechDialogFragment;
import com.muhibbin.expensenote.Features.NotesCRUD.ShowNoteList.NoteListActivity;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.ShareNote.ContactListActivity;
import com.muhibbin.expensenote.Util.Config;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailNoteListActivity extends AppCompatActivity implements DetailNoteCreateListener {

    private static final String TAG = "DetailNoteListActivity";
    private long noteRegNo;

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<DetailNote> detailNoteList = new ArrayList<>();

    private TextView summaryTextView;
    private LinearLayout subjectListEmptyTextView;
    private TextView cost_tv;
    private RecyclerView recyclerView;
    private DetailNoteListRecyclerViewAdapter detailNoteListRecyclerViewAdapter;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Config.NOTE_NAME1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        subjectListEmptyTextView = findViewById(R.id.emptyListTextView);
        cost_tv = findViewById(R.id.cost_tv);

//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("shared_data");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        noteRegNo = getIntent().getLongExtra(Config.NOTE_ID1, -1);

        detailNoteList.addAll(databaseQueryClass.getAllSubjectsByRegNo(noteRegNo));

//        double total_cost = 0;
//        for (int i = 0; i < detailNoteList.size() ; i++) {
//            Log.d("TAGqa", "onCreate: "+detailNoteList.get(i).getId()+" "+detailNoteList.get(i).getActivated());
//        }
        setTotalCostText();

//        Locale current = getResources().getConfiguration().locale;
//        Log.i("locale", Currency.getInstance(current).getSymbol());
        Currency currency = Currency.getInstance(Locale.getDefault());
        String currencyCode = currency. getCurrencyCode();
        Log.d("locale", "onCreate: "+currencyCode);

//        cost_tv.setText(NumberFormat.getInstance().format(total_cost));
        detailNoteListRecyclerViewAdapter = new DetailNoteListRecyclerViewAdapter(this, detailNoteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(detailNoteListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubjectCreateDialog();
            }
        });
        FloatingActionButton fabMic = findViewById(R.id.fabMic);
        fabMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getSpeechInput();
                PermissionX.init(DetailNoteListActivity.this)
                        .permissions(Manifest.permission.RECORD_AUDIO).request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if (allGranted) {
//                                    Toast.makeText(getApplicationContext(), "All permissions are granted", Toast.LENGTH_LONG).show();
                                    speechDialog();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Permission denied. Permission needed in order to speak", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });
    }

    private void speechDialog() {
        DetailNoteCreateSpeechDialogFragment detailNoteCreateSpeechDialogFragment = DetailNoteCreateSpeechDialogFragment.newInstance(noteRegNo, this);
        detailNoteCreateSpeechDialogFragment.show(getSupportFragmentManager(), Config.CREATE_DETAIL);

    }

//    private void getSpeechInput() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 10);
////            setResult(10, intent);
////            finish();
//        } else {
//            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(DetailNoteListActivity.this, "" + result.get(0), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    switch (result.getResultCode()) {
//                        case 10:
//                            if (result.getResultCode() == 10 && result.getData() != null) {
//                                ArrayList<String> text = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                                Toast.makeText(DetailNoteListActivity.this, ""+text.get(0), Toast.LENGTH_SHORT).show();
//
//                            }
//                            break;
//                    }
//                    if (result.getResultCode() == 123) {
//                        // ToDo : Do your stuff...
//                    } else if(result.getResultCode() == 321) {
//                        // ToDo : Do your stuff...
//                    }
//                }
//            });
    private void printSummary() {
        long noteNum = databaseQueryClass.getNumberOfNote();
        long subjectNum = databaseQueryClass.getNumberOfSubject();

        summaryTextView.setText(getResources().getString(R.string.database_summary, noteNum, subjectNum));
    }

    private void openSubjectCreateDialog() {
        DetailNoteCreateDialogFragment detailNoteCreateDialogFragment = DetailNoteCreateDialogFragment.newInstance(noteRegNo, this);
        detailNoteCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_DETAIL);

    }

    @Override
    public void onSubjectCreated(DetailNote detailNote) {
        detailNoteList.add(detailNote);
        detailNoteListRecyclerViewAdapter.notifyDataSetChanged();
        NoteListActivity.noteListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (detailNoteList.isEmpty())
            subjectListEmptyTextView.setVisibility(View.VISIBLE);
        else
            subjectListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share_list:
                Intent contactListActivity = new Intent(this, ContactListActivity.class);
//                startActivity(noteListActivity);
                contactListActivityResultLauncher.launch(contactListActivity);
//                uploadToFirebase();
                break;
            case R.id.delete_item:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete all items?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                boolean isAllDeleted = databaseQueryClass.deleteAllSubjectsByRegNum(noteRegNo);
                                if (isAllDeleted) {
                                    detailNoteList.clear();
                                    detailNoteListRecyclerViewAdapter.notifyDataSetChanged();
                                    viewVisibility();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ActivityResultLauncher<Intent> contactListActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        String toNumber = result.getData().getStringExtra("toNumber");;
                        uploadToFirebase(toNumber);
                    }
                }
            });
    private void uploadToFirebase(String toNumber) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", NULL);

        Map<String, Object> values = new HashMap<>();
        values.put("title", getIntent().getStringExtra(Config.NOTE_NAME1));
        values.put("from", phoneNumber);
        values.put("item",detailNoteList);




        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onDataChange: "+data.child("phone").getValue());

                        if (data.child("phone").getValue().toString().contains(toNumber)) {
                            //do ur stuff
                            Log.d(TAG, "onDataChange: " + data.getValue());
                            Log.d(TAG, "onDataChange: " + data.getKey());

//                            contactListFinal.add(contactList.get(i));
                            final String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(data.getKey()).child("shared_list").child(uploadId).setValue(values);

                        } else {
                            //do something if not exists

                        }
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void setTotalCostText() {
        double total_cost = 0;
        for (int i = 0; i < detailNoteList.size(); i++) {

                total_cost += detailNoteList.get(i).getPrice();

        }
        cost_tv.setText(NumberFormat.getInstance().format(total_cost));
    }
}
