package com.muhibbin.expensenote.ShareNote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.muhibbin.expensenote.Database.DatabaseQueryClass;

import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class SharedNoteListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Note> noteList;
    private DatabaseQueryClass databaseQueryClass;

    public SharedNoteListRecyclerViewAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Note note = noteList.get(position);
//        int progress = 0;
//        List<DetailNote> detailNoteList = new ArrayList<>();
//        detailNoteList.addAll(databaseQueryClass.getAllSubjectsByRegNo(note.getId()));
//        int total_active = 0;
//        for (int i = 0; i < detailNoteList.size(); i++) {
//            total_active += detailNoteList.get(i).getActivated();
////            Log.d("TAGb", "onBindViewHolder: "+detailNoteList.get(i).getActivated());
//        }
        holder.nameTextView.setText(note.getName());
//        holder.simpleProgressBar.setMax(detailNoteList.size());
//        holder.simpleProgressBar.setProgress(total_active);
//        holder.progress_text.setText(total_active + "/" + detailNoteList.size());


//        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this note?");
//                alertDialogBuilder.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                deleteNote(itemPosition);
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, DetailNoteListActivity.class);
//                intent.putExtra(Config.NOTE_ID1, note.getId());
//                intent.putExtra(Config.NOTE_NAME1, note.getName());
//                context.startActivity(intent);
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final CharSequence[] items = {"Edit", "Delete"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                builder.setTitle("Select an action");
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        switch (item) {
//                            case 0:
//                                NoteUpdateDialogFragment noteUpdateDialogFragment = NoteUpdateDialogFragment.newInstance(note.getId(), itemPosition, new NoteUpdateListener() {
//                                    @Override
//                                    public void onNoteInfoUpdated(Note note, int position) {
//                                        noteList.set(position, note);
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                                noteUpdateDialogFragment.show(((NoteListActivity) context).getSupportFragmentManager(), Config.UPDATE_NOTE);
//
//                                break;
//                            case 1:
//
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this note?");
//                                alertDialogBuilder.setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface arg0, int arg1) {
//                                                deleteNote(itemPosition);
//                                            }
//                                        });
//
//                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//
//                                break;
//
//                        }
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
    }

//    private void deleteNote(int position) {
//        Note note = noteList.get(position);
//        long count = databaseQueryClass.deleteNoteByRegNum(note.getId());
//
//        if (count > 0) {
//            noteList.remove(position);
//            notifyDataSetChanged();
//            ((NoteListActivity) context).viewVisibility();
//            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_LONG).show();
//        } else
//            Toast.makeText(context, "Note not deleted. Something wrong!", Toast.LENGTH_LONG).show();
//
//    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
