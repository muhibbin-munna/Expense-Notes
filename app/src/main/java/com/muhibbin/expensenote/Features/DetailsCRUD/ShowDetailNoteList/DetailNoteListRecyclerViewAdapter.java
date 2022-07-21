package com.muhibbin.expensenote.Features.DetailsCRUD.ShowDetailNoteList;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNote;
import com.muhibbin.expensenote.Features.DetailsCRUD.UpdateSubjectInfo.SubjectUpdateDialogFragment;
import com.muhibbin.expensenote.Features.DetailsCRUD.UpdateSubjectInfo.SubjectUpdateListener;
import com.muhibbin.expensenote.Features.NotesCRUD.ShowNoteList.NoteListActivity;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;

import java.text.NumberFormat;
import java.util.List;

public class DetailNoteListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<DetailNote> detailNoteList;

    public DetailNoteListRecyclerViewAdapter(Context context, List<DetailNote> detailNoteList) {
        this.context = context;
        this.detailNoteList = detailNoteList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int listPosition = position;
        final DetailNote detailNote = detailNoteList.get(position);

        holder.detailsNoteNameTV.setText(detailNote.getName());
//        holder.subjectCodeTextView.setText(String.valueOf(subject.getCode()));
//        holder.subjectCreditTextView.setText(String.valueOf(subject.getCredit()));
        holder.priceTv.setText(NumberFormat.getInstance().format(detailNote.getPrice()));
//        Log.d("TAGaf", "onBindViewHolder: ac"+ detailNote.getActivated());
        if (detailNote.getActivated() == 1) {
            holder.subjectNameCheckBox.setChecked(true);
            holder.detailsNoteNameTV.setTextColor(ContextCompat.getColor(context, R.color.grey_60));
            holder.detailsNoteNameTV.setPaintFlags(holder.detailsNoteNameTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceTv.setTextColor(ContextCompat.getColor(context, R.color.grey_60));
//            holder.priceTv.setPaintFlags(holder.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.subjectNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                detailNoteList.get(listPosition).setActivated(b ? 1 : 0);
//                ((DetailNoteListActivity) context).setTotalCostText();
//                Log.d("TAGaf", "onCheckedChanged: id "+detailNoteList.get(listPosition).getId());

                if(b) {
                    holder.detailsNoteNameTV.setTextColor(ContextCompat.getColor(context, R.color.grey_60));
                    holder.detailsNoteNameTV.setPaintFlags(holder.detailsNoteNameTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.priceTv.setTextColor(ContextCompat.getColor(context, R.color.grey_60));
//                    holder.priceTv.setPaintFlags(holder.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    holder.detailsNoteNameTV.setTextColor(ContextCompat.getColor(context, R.color.grey_90));
                    holder.detailsNoteNameTV.setPaintFlags( holder.detailsNoteNameTV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.priceTv.setTextColor(ContextCompat.getColor(context, R.color.grey_90));
//                    holder.priceTv.setPaintFlags( holder.priceTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
                long row = databaseQueryClass.updateSubjectInfo(detailNoteList.get(listPosition));
                NoteListActivity.noteListRecyclerViewAdapter.notifyDataSetChanged();



//                notifyDataSetChanged();


            }
        });

//        holder.subjectNameTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editSubject(detailNote.getId(), listPosition);
//            }
//        });
//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSubject(detailNote.getId(), listPosition);
//                holder.subjectNameCheckBox.setChecked(!holder.subjectNameCheckBox.isChecked());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final CharSequence[] items = {"Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Select an action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                editSubject(detailNote.getId(), listPosition);
                                break;
                            case 1:

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this item?");
                                alertDialogBuilder.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                deleteSubject(detailNote);
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

                                break;

                        }
                    }
                });
                builder.show();
                return true;
            }
        });
//        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this subject?");
//                alertDialogBuilder.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                deleteSubject(detailNote);
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
//
//        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editSubject(detailNote.getId(), listPosition);
//            }
//        });
    }

    private void editSubject(long subjectId, int listPosition) {
        SubjectUpdateDialogFragment subjectUpdateDialogFragment = SubjectUpdateDialogFragment.newInstance(subjectId, listPosition, new SubjectUpdateListener() {
            @Override
            public void onSubjectInfoUpdate(DetailNote subject, int position) {
                detailNoteList.set(position, subject);
//                ((DetailNoteListActivity) context).setTotalCostText();
                notifyDataSetChanged();
                NoteListActivity.noteListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        subjectUpdateDialogFragment.show(((DetailNoteListActivity) context).getSupportFragmentManager(), Config.UPDATE_DETAIL);
    }

    private void deleteSubject(DetailNote detailNote) {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
        boolean isDeleted = databaseQueryClass.deleteSubjectById(detailNote.getId());

        if (isDeleted) {
            detailNoteList.remove(detailNote);
            notifyDataSetChanged();
            NoteListActivity.noteListRecyclerViewAdapter.notifyDataSetChanged();
//            ((DetailNoteListActivity) context).setTotalCostText();
            ((DetailNoteListActivity) context).viewVisibility();
        } else
            Toast.makeText(context, "Cannot delete!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return detailNoteList.size();
    }
}
